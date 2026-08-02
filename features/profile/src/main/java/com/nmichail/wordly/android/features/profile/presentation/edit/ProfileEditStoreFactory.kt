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
				ProfileEditComponent.State,
				ProfileEditComponent.Label,
				> by storeFactory.create(
				name = "ProfileEditStore",
				initialState = ProfileEditComponent.State.Loading,
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

	private object ReducerImpl : Reducer<ProfileEditComponent.State, Msg> {

		override fun ProfileEditComponent.State.reduce(msg: Msg): ProfileEditComponent.State =
			when (msg) {
				Msg.Loading -> ProfileEditComponent.State.Loading
				Msg.SetError -> ProfileEditComponent.State.Error
				is Msg.Loaded -> ProfileEditComponent.State.Content(
					email = msg.profile.email,
					firstName = msg.profile.firstName,
					lastName = msg.profile.lastName,
					englishLevel = msg.profile.englishLevel,
					saving = false,
					saved = false,
				)
				is Msg.FirstNameChanged -> contentOrThis {
					copy(firstName = msg.value, saved = false)
				}
				is Msg.LastNameChanged -> contentOrThis {
					copy(lastName = msg.value, saved = false)
				}
				is Msg.EnglishLevelChanged -> contentOrThis {
					copy(englishLevel = msg.value, saved = false)
				}
				is Msg.Saving -> contentOrThis { copy(saving = msg.saving) }
				Msg.Saved -> contentOrThis { copy(saving = false, saved = true) }
			}

		private fun ProfileEditComponent.State.contentOrThis(
			update: ProfileEditComponent.State.Content.() -> ProfileEditComponent.State.Content,
		): ProfileEditComponent.State {
			val content = this as? ProfileEditComponent.State.Content ?: return this
			return content.update()
		}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ProfileEditStore.Intent,
			Action,
			ProfileEditComponent.State,
			Msg,
			ProfileEditComponent.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> load()
			}
		}

		override fun executeIntent(intent: ProfileEditStore.Intent) {
			when (intent) {
				ProfileEditStore.Intent.Retry -> load()
				ProfileEditStore.Intent.Back -> publish(ProfileEditComponent.Label.Close)
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
			val content = state() as? ProfileEditComponent.State.Content ?: return
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