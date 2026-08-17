package com.nmichail.wordly.android.features.words.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.presentation.WordDetailDialogState

interface WordDetailStore :
	Store<WordDetailStore.Intent, WordDetailStore.State, WordDetailStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Closed : State

		data class Open(val dialog: WordDetailDialogState) : State
	}

	sealed interface CalendarAction {

		data object Open : CalendarAction

		data object Dismiss : CalendarAction

		data object PreviousMonth : CalendarAction

		data object NextMonth : CalendarAction

		data object Today : CalendarAction

		data class DayClick(val dayOfMonth: Int) : CalendarAction
	}

	sealed interface Label {

		data object Dismiss : Label

		data object Changed : Label
	}

	sealed interface Intent {

		data class Open(val word: WordItem) : Intent

		data object Dismiss : Intent

		data class ChangeStatus(val status: WordStatus) : Intent

		data class Calendar(val action: CalendarAction) : Intent

		data object ConfirmAddToReview : Intent

		data object PlayAudio : Intent
	}
}
