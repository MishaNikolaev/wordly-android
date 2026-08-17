package com.nmichail.wordly.android.features.cards.training.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeWord

interface CardPracticeStore :
	Store<CardPracticeStore.Intent, CardPracticeStore.State, CardPracticeStore.Label> {

	sealed interface Intent {

		data object Close : Intent

		data object Retry : Intent

		data object PlayAudio : Intent

		data class SelectOption(val optionId: String) : Intent

		data object Continue : Intent

		data object Finish : Intent
	}

	sealed interface State {

		data object Initial : State

		data object Loading : State

		sealed interface Content : State {

			val correctCount: Int

			val totalCount: Int

			data class InProgress(
				val words: List<CardPracticeWord>,
				val currentIndex: Int,
				val currentWord: CardPracticeWord,
				override val totalCount: Int,
				val progressIndex: Int,
				val selectedOptionId: String?,
				val answerRevealed: Boolean,
				val correct: Boolean,
				override val correctCount: Int,
			) : Content

			data class Finished(
				override val correctCount: Int,
				override val totalCount: Int,
			) : Content
		}

		data object Error : State
	}

	sealed interface Label {

		data object Close : Label
	}
}
