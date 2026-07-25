package com.nmichail.wordly.android.features.cards.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import javax.inject.Inject

internal class DefaultCardsComponentFactory @Inject constructor(
	private val cardsStoreFactory: CardsStoreFactory,
) : CardsComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		cardsRouter: CardsRouter,
		onCardClick: (CardsItem) -> Unit,
	): CardsComponent =
		DefaultCardsComponent(
			componentContext = componentContext,
			cardsStoreFactory = cardsStoreFactory,
			cardsRouter = cardsRouter,
			onCardClick = onCardClick,
		)
}