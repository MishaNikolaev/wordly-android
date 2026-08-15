package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.core.preferences.domain.usecase.SetThemeModeUseCase
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.domain.usecase.GetProfileUseCase
import com.nmichail.wordly.android.features.profile.domain.usecase.LogoutUseCase
import com.nmichail.wordly.android.features.profile.domain.usecase.UpdateProfileUseCase
import com.nmichail.wordly.android.shared.error.presentation.ErrorLogoutRouter
import javax.inject.Inject

internal class ProfileStoreFactory @Inject constructor(
	private val getProfileUseCase: GetProfileUseCase,
	private val updateProfileUseCase: UpdateProfileUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase,
	private val setThemeModeUseCase: SetThemeModeUseCase,
	private val errorLogoutRouter: ErrorLogoutRouter,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ProfileStore =
		object :
			ProfileStore,
			Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> by storeFactory.create(
				name = "ProfileStore",
				initialState = ProfileStore.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data object SetError : Msg

		data class ProfileLoaded(val profile: UserProfile) : Msg

		data class ProfileUpdated(val profile: UserProfile) : Msg

		data class NotificationsEnabled(val enabled: Boolean) : Msg

		data class LoggingOut(val loggingOut: Boolean) : Msg
	}

	private object ReducerImpl : Reducer<ProfileStore.State, Msg> {

		override fun ProfileStore.State.reduce(msg: Msg): ProfileStore.State {
			val content = this as? ProfileStore.State.Content
			return when (msg) {
				Msg.Loading -> ProfileStore.State.Loading
				Msg.SetError -> ProfileStore.State.Error
				is Msg.ProfileLoaded -> ProfileStore.State.Content(
					profile = msg.profile,
					notificationsEnabled = content?.notificationsEnabled ?: true,
					loggingOut = false,
				)
				is Msg.ProfileUpdated -> content?.copy(profile = msg.profile) ?: this
				is Msg.NotificationsEnabled -> content?.copy(notificationsEnabled = msg.enabled) ?: this
				is Msg.LoggingOut -> content?.copy(loggingOut = msg.loggingOut) ?: this
			}
		}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ProfileStore.Intent,
			Action,
			ProfileStore.State,
			Msg,
			ProfileStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> load(showLoading = true)
			}
		}

		override fun executeIntent(intent: ProfileStore.Intent) {
			when (intent) {
				ProfileStore.Intent.Retry -> load(showLoading = true)
				ProfileStore.Intent.Refresh -> load(showLoading = false)
				ProfileStore.Intent.OpenEdit -> publish(ProfileStore.Label.OpenEdit)
				is ProfileStore.Intent.UpdateLevel -> updateLevel(intent.level)
				ProfileStore.Intent.ToggleNotificationsEnabled -> toggleNotificationsEnabled()
				is ProfileStore.Intent.UpdateDailyGoal -> updateDailyGoal(intent.goal)
				is ProfileStore.Intent.UpdateNotificationTimes -> {
					updateNotificationTimes(intent.times)
				}
				is ProfileStore.Intent.SetThemeMode -> setThemeModeUseCase(intent.mode)
				ProfileStore.Intent.Logout -> logout()
			}
		}

		private fun load(showLoading: Boolean) {
			if (showLoading) {
				dispatch(Msg.Loading)
			}
			launchTry {
				dispatch(Msg.ProfileLoaded(profile = getProfileUseCase()))
			} catch {
				if (showLoading || state() !is ProfileStore.State.Content) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun updateLevel(level: String) {
			val content = state() as? ProfileStore.State.Content ?: return
			launchTry {
				val updated = updateProfileUseCase(
					params = UpdateProfileParams(
						firstName = content.profile.firstName,
						lastName = content.profile.lastName,
						englishLevel = level,
						dailyGoalWords = null,
						notificationTimes = null,
					),
				)
				dispatch(Msg.ProfileUpdated(profile = updated))
			} catch {
				// ignored
			}
		}

		private fun toggleNotificationsEnabled() {
			val content = state() as? ProfileStore.State.Content ?: return
			// TODO: запросить/отозвать системные уведомления и синхронизировать с бэком
			dispatch(Msg.NotificationsEnabled(enabled = !content.notificationsEnabled))
		}

		private fun updateDailyGoal(goal: DailyGoal) {
			val content = state() as? ProfileStore.State.Content ?: return
			launchTry {
				val updated = updateProfileUseCase(
					params = content.profile.toUpdateParams(
						dailyGoalWords = goal.wordsPerDay,
						notificationTimes = null,
					),
				)
				dispatch(Msg.ProfileUpdated(profile = updated))
			} catch {
				// ignored
			}
		}

		private fun updateNotificationTimes(times: List<String>) {
			val content = state() as? ProfileStore.State.Content ?: return
			launchTry {
				val updated = updateProfileUseCase(
					params = content.profile.toUpdateParams(
						dailyGoalWords = null,
						notificationTimes = times.sorted(),
					),
				)
				dispatch(Msg.ProfileUpdated(profile = updated))
			} catch {
				// ignored
			}
		}

		private fun logout() {
			dispatch(Msg.LoggingOut(loggingOut = true))
			launchTry {
				runCatching { logoutUseCase() }
				clearAuthTokensUseCase()
				dispatch(Msg.LoggingOut(loggingOut = false))
				errorLogoutRouter.navigateToLogoutScreen(userBlocked = false)
			} catch {
				clearAuthTokensUseCase()
				dispatch(Msg.LoggingOut(loggingOut = false))
				errorLogoutRouter.navigateToLogoutScreen(userBlocked = false)
			}
		}
	}
}

private fun UserProfile.toUpdateParams(
	dailyGoalWords: Int?,
	notificationTimes: List<String>?,
): UpdateProfileParams =
	UpdateProfileParams(
		firstName = firstName,
		lastName = lastName,
		englishLevel = englishLevel,
		dailyGoalWords = dailyGoalWords,
		notificationTimes = notificationTimes,
	)
