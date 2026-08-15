package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.mvikotlin.core.store.Store

import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.entity.WordTag

interface WordsStore :
	Store<WordsStore.Intent, WordsStore.State, Nothing> {

	sealed interface State {

		data object Loading : State

		data class Content(
			val title: String,
			val searchQuery: String,
			val searchPlaceholder: String,
			val selectedFilter: WordFilter,
			val words: List<WordItem>,
			val tags: List<WordTag>,
			val addWordDialog: AddWordDialogState?,
			val wordDetailDialog: WordDetailDialogState?,
		) : State

		data object Error : State
	}

	sealed interface CalendarAction {

		data object Open : CalendarAction

		data object Dismiss : CalendarAction

		data object PreviousMonth : CalendarAction

		data object NextMonth : CalendarAction

		data object Today : CalendarAction

		data class DayClick(val dayOfMonth: Int) : CalendarAction
	}

	sealed interface Intent {

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class ChangeFilter(val filter: WordFilter) : Intent

		data object OpenAddWord : Intent

		data object DismissAddWord : Intent

		data class ChangeWordInput(val value: String) : Intent

		data class ToggleTag(val tagId: String) : Intent

		data object ConfirmAddWord : Intent

		data class OpenWordDetail(val wordId: String) : Intent

		data object DismissWordDetail : Intent

		data class ChangeDetailStatus(val status: WordStatus) : Intent

		data class Calendar(val action: CalendarAction) : Intent

		data object ConfirmAddToReview : Intent

		data object PlayAudio : Intent
	}
}