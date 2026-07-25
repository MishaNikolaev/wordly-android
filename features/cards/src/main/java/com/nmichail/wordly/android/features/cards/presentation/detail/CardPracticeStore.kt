package com.nmichail.wordly.android.features.cards.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store

internal interface CardPracticeStore :
	Store<CardPracticeStore.Intent, CardPracticeComponent.State, CardPracticeComponent.Label> {

	sealed interface Intent {

		data object Close : Intent

		data object Retry : Intent

		data object PlayAudio : Intent

		data class SelectOption(val optionId: String) : Intent

		data object Continue : Intent

		data object Finish : Intent
	}
}