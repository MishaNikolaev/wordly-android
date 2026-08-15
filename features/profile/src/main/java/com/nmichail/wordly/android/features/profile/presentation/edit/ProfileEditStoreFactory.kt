package com.nmichail.wordly.android.features.profile.presentation.edit

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.profile.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.domain.usecase.GetProfileUseCase
import com.nmichail.wordly.android.features.profile.domain.usecase.UpdateProfileUseCase
import javax.inject.Inject

internal class ProfileEditStoreFactory @Inject constructor(
	private val getProfileUseCase: GetProfileUseCase,
	private val updateProfileUseCase: UpdateProfileUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ProfileEditStore =
		object :
			ProfileEditStore,
			Store<
				ProfileEditStore.Intent,
				ProfileEditStore.State,
				ProfileEditStore.Label,
				> by storeFactory.create(
				name = "ProfileEditStore",
				initialState = ProfileEditStore.State.Loading,
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

		data class Loaded(val profile: UserProfile) : Msg

		data class FirstNameChanged(val value: String) : Msg

		data class LastNameChanged(val value: String) : Msg

		data class EnglishLevelChanged(val value: String) : Msg

		data class Saving(val saving: Boolean) : Msg

		data object Saved : Msg
	}

	private object ReducerImpl : Reducer<ProfileEditStore.State, Msg> {

		override fun ProfileEditStore.State.reduce(msg: Msg): ProfileEditStore.State {
			val content = this as? ProfileEditStore.State.Content
			return when (msg) {
				Msg.Loading -> ProfileEditStore.State.Loading
				Msg.SetError -> ProfileEditStore.State.Error
				is Msg.Loaded -> ProfileEditStore.State.Content(
					email = msg.profile.email,
					firstName = msg.profile.firstName,
					lastName = msg.profile.lastName,
					englishLevel = msg.profile.englishLevel,
					saving = false,
					saved = false,
				)
				is Msg.FirstNameChanged -> content?.copy(firstName = msg.value, saved = false) ?: this
				is Msg.LastNameChanged -> content?.copy(lastName = msg.value, saved = false) ?: this
				is Msg.EnglishLevelChanged -> content?.copy(englishLevel = msg.value, saved = false) ?: this
				is Msg.Saving -> content?.copy(saving = msg.saving) ?: this
				Msg.Saved -> content?.copy(saving = false, saved = true) ?: this
			}
		}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ProfileEditStore.Intent,
			Action,
			ProfileEditStore.State,
			Msg,
			ProfileEditStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> load()
			}
		}

		override fun executeIntent(intent: ProfileEditStore.Intent) {
			when (intent) {
				ProfileEditStore.Intent.Retry -> load()
				ProfileEditStore.Intent.Back -> publish(ProfileEditStore.Label.Close)
				is ProfileEditStore.Intent.ChangeFirstName -> {
					dispatch(Msg.FirstNameChanged(value = intent.value))
				}
				is ProfileEditStore.Intent.ChangeLastName -> {
					dispatch(Msg.LastNameChanged(value = intent.value))
				}
				is ProfileEditStore.Intent.ChangeEnglishLevel -> {
					dispatch(Msg.EnglishLevelChanged(value = intent.value))
				}
				ProfileEditStore.Intent.Save -> save()
			}
		}

		private fun load() {
			dispatch(Msg.Loading)
			launchTry {
				dispatch(Msg.Loaded(profile = getProfileUseCase()))
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun save() {
			val content = state() as? ProfileEditStore.State.Content ?: return
			if (content.saving) return
			dispatch(Msg.Saving(saving = true))
			launchTry {
				updateProfileUseCase(
					params = UpdateProfileParams(
						firstName = content.firstName.trim(),
						lastName = content.lastName.trim(),
						englishLevel = content.englishLevel,
						dailyGoalWords = null,
						notificationTimes = null,
					),
				)
				dispatch(Msg.Saved)
			} catch {
				dispatch(Msg.Saving(saving = false))
			}
		}
	}
}