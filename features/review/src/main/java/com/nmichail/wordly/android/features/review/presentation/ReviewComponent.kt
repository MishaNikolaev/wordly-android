package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface ReviewComponent {

	val model: Value<ReviewStore.State>

	fun handleClose()

	fun handleRetry()

	fun handlePlayAudio()

	fun handleSelectOption(optionId: String)

	fun handleContinue()

	fun handleFinish()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			reviewRouter: ReviewRouter,
		): ReviewComponent
	}
}