package com.nmichail.wordly.android.features.cards.presentation.detail

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultCardPracticeComponentFactory @Inject constructor(
	private val cardPracticeStoreFactory: CardPracticeStoreFactory,
) : CardPracticeComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		cardId: String,
		cardPracticeRouter: CardPracticeRouter,
	): CardPracticeComponent =
		DefaultCardPracticeComponent(
			componentContext = componentContext,
			cardId = cardId,
			cardPracticeStoreFactory = cardPracticeStoreFactory,
			cardPracticeRouter = cardPracticeRouter,
		)
}