@file:Suppress("TooManyFunctions")

package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus

internal class DefaultWordsComponent(
	componentContext: ComponentContext,
	wordsStoreFactory: WordsStoreFactory,
) : ComponentContext by componentContext,
	WordsComponent {

	private val store: WordsStore = instanceKeeper.getStore {
		wordsStoreFactory.create()
	}

	override val model: Value<WordsComponent.State> = store.asValue()

	override fun handleRetry() {
		store.accept(WordsStore.Intent.Retry)
	}

	override fun handleSearchQueryChange(query: String) {
		store.accept(WordsStore.Intent.ChangeSearchQuery(query = query))
	}

	override fun handleFilterChange(filter: WordFilter) {
		store.accept(WordsStore.Intent.ChangeFilter(filter = filter))
	}

	override fun handleOpenAddWord() {
		store.accept(WordsStore.Intent.OpenAddWord)
	}

	override fun handleDismissAddWord() {
		store.accept(WordsStore.Intent.DismissAddWord)
	}

	override fun handleAddWordInputChange(value: String) {
		store.accept(WordsStore.Intent.ChangeWordInput(value = value))
	}

	override fun handleToggleTag(tagId: String) {
		store.accept(WordsStore.Intent.ToggleTag(tagId = tagId))
	}

	override fun handleConfirmAddWord() {
		store.accept(WordsStore.Intent.ConfirmAddWord)
	}

	override fun handleOpenWordDetail(wordId: String) {
		store.accept(WordsStore.Intent.OpenWordDetail(wordId = wordId))
	}

	override fun handleDismissWordDetail() {
		store.accept(WordsStore.Intent.DismissWordDetail)
	}

	override fun handleDetailStatusChange(status: WordStatus) {
		store.accept(WordsStore.Intent.ChangeDetailStatus(status = status))
	}

	override fun handleConfirmAddToReview() {
		store.accept(WordsStore.Intent.ConfirmAddToReview)
	}

	override fun handlePlayAudio() {
		store.accept(WordsStore.Intent.PlayAudio)
	}

	override fun handleCalendar(action: WordsComponent.CalendarAction) {
		store.accept(WordsStore.Intent.Calendar(action = action))
	}
}