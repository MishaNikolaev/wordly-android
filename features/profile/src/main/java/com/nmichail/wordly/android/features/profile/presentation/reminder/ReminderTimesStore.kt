package com.nmichail.wordly.android.features.profile.presentation.reminder

import com.arkivanov.mvikotlin.core.store.Store

interface ReminderTimesStore :
	Store<ReminderTimesStore.Intent, ReminderTimesStore.State, ReminderTimesStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Loading : State

		data class Content(
			val firstName: String,
			val lastName: String,
			val englishLevel: String,
			val selectedTimes: Set<String>,
			val saving: Boolean,
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object Close : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data object Back : Intent

		data class ToggleTime(val time: String) : Intent

		data object Save : Intent
	}
}
