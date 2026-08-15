package com.nmichail.wordly.android.features.cards.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem

interface CardsComponent {

	val model: Value<CardsStore.State>

	fun handleBack()

	fun handleRetry()

	fun handleSearchQueryChange(query: String)

	fun handleLevelChange(level: String)

	fun handleCardClick(cardId: String)

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			cardsRouter: CardsRouter,
			onCardClick: (CardsItem) -> Unit,
		): CardsComponent
	}
}
