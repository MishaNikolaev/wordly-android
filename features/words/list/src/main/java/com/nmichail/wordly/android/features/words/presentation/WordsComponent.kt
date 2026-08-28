package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStore
import com.nmichail.wordly.android.features.words.presentation.dialog.AddWordStore
import com.nmichail.wordly.android.features.words.presentation.list.WordsListStore

interface WordsComponent {

	val listStore: WordsListStore

	val addWordStore: AddWordStore

	val wordDetailStore: WordDetailStore

	val listModel: Value<WordsListStore.State>

	val addWordModel: Value<AddWordStore.State>

	val wordDetailModel: Value<WordDetailStore.State>

	fun openAddWord()

	fun openWordDetail(wordId: String)

	fun handleRefresh()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onCatalogChanged: () -> Unit,
		): WordsComponent
	}
}
