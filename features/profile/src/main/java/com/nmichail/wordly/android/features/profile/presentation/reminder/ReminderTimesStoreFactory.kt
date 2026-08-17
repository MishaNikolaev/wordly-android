package com.nmichail.wordly.android.features.profile.presentation.reminder

import kotlinx.coroutines.launch
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

internal class ReminderTimesStoreFactory @Inject constructor(
	private val getProfileUseCase: GetProfileUseCase,
	private val updateProfileUseCase: UpdateProfileUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ReminderTimesStore =
		object :
			ReminderTimesStore,
			Store<
				ReminderTimesStore.Intent,
				ReminderTimesStore.State,
				ReminderTimesStore.Label,
				> by storeFactory.create(
				name = "ReminderTimesStore",
				initialState = ReminderTimesStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Init : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data object SetError : Msg

		data class Loaded(val profile: UserProfile) : Msg

		data class TimeToggled(val time: String) : Msg

		data class Saving(val saving: Boolean) : Msg

		data object Saved : Msg
	}

	private object ReducerImpl : Reducer<ReminderTimesStore.State, Msg> {

		override fun ReminderTimesStore.State.reduce(msg: Msg): ReminderTimesStore.State {
			val content = this as? ReminderTimesStore.State.Content
			return when (msg) {
				Msg.Loading -> ReminderTimesStore.State.Loading
				Msg.SetError -> ReminderTimesStore.State.Error
				is Msg.Loaded -> ReminderTimesStore.State.Content(
					firstName = msg.profile.firstName,
					lastName = msg.profile.lastName,
					englishLevel = msg.profile.englishLevel,
					selectedTimes = msg.profile.notificationTimes.map { it.time }.toSet(),
					saving = false,
				)
				is Msg.TimeToggled -> content?.toggleTime(time = msg.time) ?: this
				is Msg.Saving -> content?.copy(saving = msg.saving) ?: this
				Msg.Saved -> content?.copy(saving = false) ?: this
			}
		}

		private fun ReminderTimesStore.State.Content.toggleTime(
			time: String,
		): ReminderTimesStore.State.Content {
			if (saving) return this
			val selected = selectedTimes.toMutableSet()
			if (time in selected) {
				selected.remove(time)
			} else {
				selected.add(time)
			}
			return copy(selectedTimes = selected)
		}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ReminderTimesStore.Intent,
			Action,
			ReminderTimesStore.State,
			Msg,
			ReminderTimesStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Init -> load()
			}
		}

		override fun executeIntent(intent: ReminderTimesStore.Intent) {
			when (intent) {
				ReminderTimesStore.Intent.Retry -> load()
				ReminderTimesStore.Intent.Back -> publish(ReminderTimesStore.Label.Close)
				is ReminderTimesStore.Intent.ToggleTime -> {
					dispatch(Msg.TimeToggled(time = intent.time))
				}
				ReminderTimesStore.Intent.Save -> save()
			}
		}

		private fun load() {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					dispatch(Msg.Loaded(profile = getProfileUseCase()))
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun save() {
			val content = state() as? ReminderTimesStore.State.Content ?: return
			if (content.saving) return
			dispatch(Msg.Saving(saving = true))
			scope.launch {
				try {
					updateProfileUseCase(
						params = UpdateProfileParams(
							firstName = content.firstName,
							lastName = content.lastName,
							englishLevel = content.englishLevel,
							dailyGoalWords = null,
							notificationTimes = content.selectedTimes.toList().sorted(),
						),
					)
					dispatch(Msg.Saved)
					publish(ReminderTimesStore.Label.Close)
				} catch (_: Exception) {
					dispatch(Msg.Saving(saving = false))
				}
			}
		}
	}
}