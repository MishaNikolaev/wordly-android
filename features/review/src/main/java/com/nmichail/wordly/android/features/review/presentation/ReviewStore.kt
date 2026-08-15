package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord

interface ReviewStore :
	Store<ReviewStore.Intent, ReviewStore.State, ReviewStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Loading : State

		data class Content(
			val words: List<ReviewWord>,
			val currentIndex: Int,
			val currentWord: ReviewWord,
			val totalCount: Int,
			val progressIndex: Int,
			val selectedOptionId: String?,
			val answerRevealed: Boolean,
			val correct: Boolean,
			val correctCount: Int,
			val submitting: Boolean,
			val finished: Boolean,
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object Close : Label
	}

	sealed interface Intent {

		data object Close : Intent

		data object Retry : Intent

		data object PlayAudio : Intent

		data class SelectOption(val optionId: String) : Intent

		data object Continue : Intent

		data object Finish : Intent
	}
}