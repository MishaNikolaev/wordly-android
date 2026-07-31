package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.core.preferences.domain.usecase.GetThemeModeUseCase
import com.nmichail.wordly.android.core.preferences.domain.usecase.SetThemeModeUseCase
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoals
import com.nmichail.wordly.android.features.profile.domain.entity.EnglishLevels
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlot
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlots
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
	private val getThemeModeUseCase: GetThemeModeUseCase,
	private val setThemeModeUseCase: SetThemeModeUseCase,
	private val errorLogoutRouter: ErrorLogoutRouter,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ProfileStore =
		object :
			ProfileStore,
			Store<ProfileStore.Intent, ProfileComponent.State, ProfileComponent.Label> by storeFactory.create(
				name = "ProfileStore",
				initialState = ProfileComponent.State.Loading,
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

		data class ProfileLoaded(
			val profile: UserProfile,
			val themeMode: AppThemeMode,
		) : Msg

		data class ProfileUpdated(val profile: UserProfile) : Msg

		data class ThemeModeUpdated(val themeMode: AppThemeMode) : Msg

		data class NotificationsEnabled(val enabled: Boolean) : Msg

		data class LevelDialog(val state: ProfileComponent.LevelDialogState?) : Msg

		data class DailyGoalDialog(val state: ProfileComponent.DailyGoalDialogState?) : Msg

		data class NotificationsDialog(val state: ProfileComponent.NotificationsDialogState?) : Msg

		data class ThemeDialog(val state: ProfileComponent.ThemeDialogState?) : Msg

		data class LogoutDialog(val visible: Boolean) : Msg

		data class LoggingOut(val loggingOut: Boolean) : Msg
	}

	private object ReducerImpl : Reducer<ProfileComponent.State, Msg> {

		override fun ProfileComponent.State.reduce(msg: Msg): ProfileComponent.State =
			when (msg) {
				Msg.Loading -> ProfileComponent.State.Loading
				Msg.SetError -> ProfileComponent.State.Error
				is Msg.ProfileLoaded -> {
					val previousEnabled =
						(this as? ProfileComponent.State.Content)?.notificationsEnabled ?: true
					ProfileComponent.State.Content(
						profile = msg.profile,
						themeMode = msg.themeMode,
						notificationsEnabled = previousEnabled,
						levelDialog = null,
						dailyGoalDialog = null,
						notificationsDialog = null,
						themeDialog = null,
						logoutDialogVisible = false,
						loggingOut = false,
					)
				}
				is Msg.ProfileUpdated -> contentOrThis { copy(profile = msg.profile) }
				is Msg.ThemeModeUpdated -> contentOrThis { copy(themeMode = msg.themeMode) }
				is Msg.NotificationsEnabled -> contentOrThis { copy(notificationsEnabled = msg.enabled) }
				is Msg.LevelDialog -> contentOrThis { copy(levelDialog = msg.state) }
				is Msg.DailyGoalDialog -> contentOrThis { copy(dailyGoalDialog = msg.state) }
				is Msg.NotificationsDialog -> contentOrThis { copy(notificationsDialog = msg.state) }
				is Msg.ThemeDialog -> contentOrThis { copy(themeDialog = msg.state) }
				is Msg.LogoutDialog -> contentOrThis { copy(logoutDialogVisible = msg.visible) }
				is Msg.LoggingOut -> contentOrThis { copy(loggingOut = msg.loggingOut) }
			}

		private fun ProfileComponent.State.contentOrThis(
			update: ProfileComponent.State.Content.() -> ProfileComponent.State.Content,
		): ProfileComponent.State {
			val content = this as? ProfileComponent.State.Content ?: return this
			return content.update()
		}
	}

	@Suppress("TooManyFunctions")
	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ProfileStore.Intent,
			Action,
			ProfileComponent.State,
			Msg,
			ProfileComponent.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> load(showLoading = true)
			}
		}

		@Suppress("CyclomaticComplexMethod")
		override fun executeIntent(intent: ProfileStore.Intent) {
			when (intent) {
				ProfileStore.Intent.Retry -> load(showLoading = true)
				ProfileStore.Intent.Refresh -> load(showLoading = false)
				ProfileStore.Intent.OpenEdit -> publish(ProfileComponent.Label.OpenEdit)
				ProfileStore.Intent.OpenLevel -> openLevel()
				is ProfileStore.Intent.ConfirmLevel -> confirmLevel(intent.level)
				ProfileStore.Intent.DismissLevel -> dispatch(Msg.LevelDialog(state = null))
				ProfileStore.Intent.ToggleNotificationsEnabled -> toggleNotificationsEnabled()
				ProfileStore.Intent.OpenDailyGoal -> openDailyGoal()
				is ProfileStore.Intent.ConfirmDailyGoal -> confirmDailyGoal(intent.goal)
				ProfileStore.Intent.DismissDailyGoal -> dispatch(Msg.DailyGoalDialog(state = null))
				ProfileStore.Intent.OpenNotifications -> openNotifications()
				is ProfileStore.Intent.ToggleNotification -> toggleNotification(intent.slot)
				ProfileStore.Intent.ConfirmNotifications -> confirmNotifications()
				ProfileStore.Intent.DismissNotifications -> {
					dispatch(Msg.NotificationsDialog(state = null))
				}
				ProfileStore.Intent.OpenTheme -> openTheme()
				is ProfileStore.Intent.SelectTheme -> selectTheme(intent.mode)
				ProfileStore.Intent.ConfirmTheme -> confirmTheme()
				ProfileStore.Intent.DismissTheme -> dismissTheme()
				ProfileStore.Intent.OpenLogout -> dispatch(Msg.LogoutDialog(visible = true))
				ProfileStore.Intent.ConfirmLogout -> confirmLogout()
				ProfileStore.Intent.DismissLogout -> dispatch(Msg.LogoutDialog(visible = false))
			}
		}

		private fun load(showLoading: Boolean) {
			if (showLoading) {
				dispatch(Msg.Loading)
			}
			launchTry {
				val profile = getProfileUseCase()
				dispatch(
					Msg.ProfileLoaded(
						profile = profile,
						themeMode = getThemeModeUseCase(),
					),
				)
			} catch {
				if (showLoading || state() !is ProfileComponent.State.Content) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun openLevel() {
			val content = state() as? ProfileComponent.State.Content ?: return
			dispatch(
				Msg.LevelDialog(
					state = ProfileComponent.LevelDialogState(
						options = EnglishLevels.codes,
						selected = content.profile.englishLevel,
					),
				),
			)
		}

		private fun confirmLevel(level: String) {
			val content = state() as? ProfileComponent.State.Content ?: return
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
				dispatch(Msg.LevelDialog(state = null))
			} catch {
				// ignored
			}
		}

		private fun toggleNotificationsEnabled() {
			val content = state() as? ProfileComponent.State.Content ?: return
			// TODO: запросить/отозвать системные уведомления и синхронизировать с бэком
			dispatch(Msg.NotificationsEnabled(enabled = !content.notificationsEnabled))
		}

		private fun openDailyGoal() {
			val content = state() as? ProfileComponent.State.Content ?: return
			dispatch(
				Msg.DailyGoalDialog(
					state = ProfileComponent.DailyGoalDialogState(
						options = DailyGoals.options,
						selected = content.profile.dailyGoal,
					),
				),
			)
		}

		private fun confirmDailyGoal(goal: DailyGoal) {
			val content = state() as? ProfileComponent.State.Content ?: return
			launchTry {
				val updated = updateProfileUseCase(
					params = content.profile.toUpdateParams(
						dailyGoalWords = goal.wordsPerDay,
						notificationTimes = null,
					),
				)
				dispatch(Msg.ProfileUpdated(profile = updated))
				dispatch(Msg.DailyGoalDialog(state = null))
			} catch {
				// ignored
			}
		}

		private fun openNotifications() {
			val content = state() as? ProfileComponent.State.Content ?: return
			dispatch(
				Msg.NotificationsDialog(
					state = ProfileComponent.NotificationsDialogState(
						options = NotificationTimeSlots.options,
						selected = content.profile.notificationTimes.map { it.time }.toSet(),
					),
				),
			)
		}

		private fun toggleNotification(slot: NotificationTimeSlot) {
			val content = state() as? ProfileComponent.State.Content ?: return
			val dialog = content.notificationsDialog ?: return
			val selected = dialog.selected.toMutableSet()
			if (slot.time in selected) {
				selected.remove(slot.time)
			} else {
				selected.add(slot.time)
			}
			dispatch(Msg.NotificationsDialog(state = dialog.copy(selected = selected)))
		}

		private fun confirmNotifications() {
			val content = state() as? ProfileComponent.State.Content ?: return
			val dialog = content.notificationsDialog ?: return
			launchTry {
				val updated = updateProfileUseCase(
					params = content.profile.toUpdateParams(
						dailyGoalWords = null,
						notificationTimes = dialog.selected.sorted(),
					),
				)
				dispatch(Msg.ProfileUpdated(profile = updated))
				dispatch(Msg.NotificationsDialog(state = null))
			} catch {
				// ignored
			}
		}

		private fun openTheme() {
			val content = state() as? ProfileComponent.State.Content ?: return
			dispatch(
				Msg.ThemeDialog(
					state = ProfileComponent.ThemeDialogState(
						options = AppThemeMode.entries,
						selected = content.themeMode,
						initial = content.themeMode,
					),
				),
			)
		}

		private fun selectTheme(mode: AppThemeMode) {
			val content = state() as? ProfileComponent.State.Content ?: return
			val dialog = content.themeDialog ?: return
			setThemeModeUseCase(mode)
			dispatch(Msg.ThemeModeUpdated(themeMode = mode))
			dispatch(Msg.ThemeDialog(state = dialog.copy(selected = mode)))
		}

		private fun confirmTheme() {
			dispatch(Msg.ThemeDialog(state = null))
		}

		private fun dismissTheme() {
			val content = state() as? ProfileComponent.State.Content ?: return
			val dialog = content.themeDialog ?: return
			if (content.themeMode != dialog.initial) {
				setThemeModeUseCase(dialog.initial)
				dispatch(Msg.ThemeModeUpdated(themeMode = dialog.initial))
			}
			dispatch(Msg.ThemeDialog(state = null))
		}

		private fun confirmLogout() {
			dispatch(Msg.LoggingOut(loggingOut = true))
			launchTry {
				runCatching { logoutUseCase() }
				clearAuthTokensUseCase()
				dispatch(Msg.LogoutDialog(visible = false))
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
