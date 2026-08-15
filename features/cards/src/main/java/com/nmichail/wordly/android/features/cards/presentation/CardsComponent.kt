package com.nmichail.wordly.android.features.cards.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import com.nmichail.wordly.android.features.cards.domain.entity.CardsLevelBanner
import com.nmichail.wordly.android.features.cards.domain.entity.CardsSection

interface CardsComponent {

	val model: Value<State>

	fun handleBack()

	fun handleRetry()

	fun handleSearchQueryChange(query: String)

	fun handleLevelChange(level: String)

	fun handleCardClick(cardId: String)

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val title: String,
			val searchQuery: String,
			val searchPlaceholder: String,
			val levelBanner: CardsLevelBanner?,
			val allSections: List<CardsSection>,
			val sections: List<CardsSection>,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data class OpenCard(val item: CardsItem) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			cardsRouter: CardsRouter,
			onCardClick: (CardsItem) -> Unit,
		): CardsComponent
	}
}
