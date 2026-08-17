package com.nmichail.wordly.android.features.cards.training.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface CardPracticeComponent {

	val model: Value<CardPracticeStore.State>

	fun handleClose()

	fun handleRetry()

	fun handlePlayAudio()

	fun handleSelectOption(optionId: String)

	fun handleContinue()

	fun handleFinish()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			cardId: String,
			cardPracticeRouter: CardPracticeRouter,
		): CardPracticeComponent
	}
}