package com.nmichail.wordly.android.features.cards.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import com.nmichail.wordly.android.features.cards.domain.entity.CardsLevelBanner
import com.nmichail.wordly.android.features.cards.domain.entity.CardsSection

interface CardsStore :
	Store<CardsStore.Intent, CardsStore.State, CardsStore.Label> {

	sealed interface Intent {

		data object Back : Intent

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class SelectCard(val cardId: String) : Intent

		data class ChangeLevel(val level: String) : Intent
	}

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
}