package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.review.domain.entity.ReviewQuestion

interface ReviewComponent {

	val model: Value<State>

	fun handleClose()

	fun handlePlayAudio()

	fun handleSelectOption(optionId: String)

	data class State(
		val question: ReviewQuestion,
		val selectedOptionId: String?,
	)

	sealed interface Label {

		data object Close : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			reviewRouter: ReviewRouter,
		): ReviewComponent
	}
}
