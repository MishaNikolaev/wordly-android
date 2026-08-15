package com.nmichail.wordly.android.features.words.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStore
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStoreFactory
import com.nmichail.wordly.android.features.words.presentation.dialog.AddWordStore
import com.nmichail.wordly.android.features.words.presentation.dialog.AddWordStoreFactory
import com.nmichail.wordly.android.features.words.presentation.list.WordsListStore
import com.nmichail.wordly.android.features.words.presentation.list.WordsListStoreFactory

@Suppress("TooManyFunctions")
internal class DefaultWordsComponent(
    componentContext: ComponentContext,
    wordsListStoreFactory: WordsListStoreFactory,
    addWordStoreFactory: AddWordStoreFactory,
    wordDetailStoreFactory: WordDetailStoreFactory,
) : ComponentContext by componentContext,
	WordsComponent {

	override val listStore: WordsListStore = instanceKeeper.getStore {
		wordsListStoreFactory.create()
	}

	override val addWordStore: AddWordStore = instanceKeeper.getStore {
		addWordStoreFactory.create()
	}

	override val wordDetailStore: WordDetailStore = instanceKeeper.getStore {
		wordDetailStoreFactory.create()
	}

	override val listModel: Value<WordsListStore.State> = listStore.asValue()

	override val addWordModel: Value<AddWordStore.State> = addWordStore.asValue()

	override val wordDetailModel: Value<WordDetailStore.State> = wordDetailStore.asValue()

	init {
		componentScope().launch {
			for (label in addWordStore.labelsChannel(lifecycle)) {
				when (label) {
					AddWordStore.Label.Dismiss -> Unit
					AddWordStore.Label.WordAdded -> {
						listStore.accept(WordsListStore.Intent.Refresh)
					}
				}
			}
		}
		componentScope().launch {
			for (label in wordDetailStore.labelsChannel(lifecycle)) {
				when (label) {
					WordDetailStore.Label.Dismiss -> Unit
					WordDetailStore.Label.Changed -> {
						listStore.accept(WordsListStore.Intent.Refresh)
					}
				}
			}
		}
	}

	override fun openAddWord() {
		val content = listStore.state as? WordsListStore.State.Content ?: return
		addWordStore.accept(AddWordStore.Intent.Open(availableTags = content.tags))
	}

	override fun openWordDetail(wordId: String) {
		val content = listStore.state as? WordsListStore.State.Content ?: return
		val word = content.words.find { it.id == wordId } ?: return
		wordDetailStore.accept(WordDetailStore.Intent.Open(word = word))
	}
}
