package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStoreFactory
import com.nmichail.wordly.android.features.words.presentation.dialog.AddWordStoreFactory
import com.nmichail.wordly.android.features.words.presentation.list.WordsListStoreFactory
import javax.inject.Inject

internal class DefaultWordsComponentFactory @Inject constructor(
    private val wordsListStoreFactory: WordsListStoreFactory,
    private val addWordStoreFactory: AddWordStoreFactory,
    private val wordDetailStoreFactory: WordDetailStoreFactory,
) : WordsComponent.Factory {

	override fun invoke(componentContext: ComponentContext): WordsComponent =
		DefaultWordsComponent(
			componentContext = componentContext,
			wordsListStoreFactory = wordsListStoreFactory,
			addWordStoreFactory = addWordStoreFactory,
			wordDetailStoreFactory = wordDetailStoreFactory,
		)
}
