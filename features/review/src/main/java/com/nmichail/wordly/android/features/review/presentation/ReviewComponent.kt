package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord

interface ReviewComponent {

	val model: Value<State>

	fun handleClose()

	fun handleRetry()

	fun handlePlayAudio()

	fun handleSelectOption(optionId: String)

	fun handleContinue()

	fun handleFinish()

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class InProgress(
			val words: List<ReviewWord>,
			val currentIndex: Int,
			val currentWord: ReviewWord,
			val totalCount: Int,
			val progressIndex: Int,
			val selectedOptionId: String?,
			val isAnswerRevealed: Boolean,
			val isCorrect: Boolean,
			val correctCount: Int,
			val isSubmitting: Boolean,
		) : State

		data class Finished(
			val totalCount: Int,
			val correctCount: Int,
		) : State
	}

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