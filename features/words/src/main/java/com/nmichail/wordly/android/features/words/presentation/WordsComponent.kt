@file:Suppress("TooManyFunctions")

package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.words.add.presentation.AddWordDialogState
import com.nmichail.wordly.android.features.words.detail.presentation.WordDetailDialogState
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.entity.WordTag

interface WordsComponent {

	val model: Value<State>

	fun handleRetry()

	fun handleSearchQueryChange(query: String)

	fun handleFilterChange(filter: WordFilter)

	fun handleOpenAddWord()

	fun handleDismissAddWord()

	fun handleAddWordInputChange(value: String)

	fun handleToggleTag(tagId: String)

	fun handleConfirmAddWord()

	fun handleOpenWordDetail(wordId: String)

	fun handleDismissWordDetail()

	fun handleDetailStatusChange(status: WordStatus)

	fun handleConfirmAddToReview()

	fun handlePlayAudio()

	fun handleCalendar(action: CalendarAction)

	sealed interface CalendarAction {
		data object Open : CalendarAction
		data object Dismiss : CalendarAction
		data object PreviousMonth : CalendarAction
		data object NextMonth : CalendarAction
		data object Today : CalendarAction
		data class DayClick(val dayOfMonth: Int) : CalendarAction
	}

	sealed interface State {

		data object Loading : State

		data object Error : State

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
	}

	fun interface Factory {

		operator fun invoke(componentContext: ComponentContext): WordsComponent
	}
}