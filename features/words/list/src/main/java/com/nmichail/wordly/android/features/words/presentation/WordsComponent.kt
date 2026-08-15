package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStore
import com.nmichail.wordly.android.features.words.presentation.dialog.AddWordStore
import com.nmichail.wordly.android.features.words.presentation.list.WordsListStore

@Suppress("TooManyFunctions")
interface WordsComponent {

	val listModel: Value<WordsListStore.State>

	val addWordModel: Value<AddWordStore.State>

	val wordDetailModel: Value<WordDetailStore.State>

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

	fun handleCalendar(action: WordDetailStore.CalendarAction)

	fun interface Factory {

		operator fun invoke(componentContext: ComponentContext): WordsComponent
	}
}
