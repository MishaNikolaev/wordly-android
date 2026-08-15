package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus

internal interface WordsStore :
	Store<WordsStore.Intent, WordsComponent.State, Nothing> {

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

		data class WuiCalendar(val action: WordsComponent.CalendarAction) : Intent

		data object ConfirmAddToReview : Intent

		data object PlayAudio : Intent
	}
}