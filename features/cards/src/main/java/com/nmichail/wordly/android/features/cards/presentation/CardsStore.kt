package com.nmichail.wordly.android.features.cards.presentation

import com.arkivanov.mvikotlin.core.store.Store

internal interface CardsStore :
	Store<CardsStore.Intent, CardsComponent.State, CardsComponent.Label> {

	sealed interface Intent {

		data object Back : Intent

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class SelectCard(val cardId: String) : Intent

		data class ChangeLevel(val level: String) : Intent
	}
}