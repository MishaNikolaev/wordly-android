package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultWordsComponentFactory @Inject constructor(
	private val wordsStoreFactory: WordsStoreFactory,
) : WordsComponent.Factory {

	override fun invoke(componentContext: ComponentContext): WordsComponent =
		DefaultWordsComponent(
			componentContext = componentContext,
			wordsStoreFactory = wordsStoreFactory,
		)
}