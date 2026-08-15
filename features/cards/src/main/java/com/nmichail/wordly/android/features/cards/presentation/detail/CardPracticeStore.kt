package com.nmichail.wordly.android.features.cards.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord

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

		data object Loading : State

		data object Error : State

		data class InProgress(
			val words: List<CardPracticeWord>,
			val currentIndex: Int,
			val currentWord: CardPracticeWord,
			val totalCount: Int,
			val progressIndex: Int,
			val selectedOptionId: String?,
			val answerRevealed: Boolean,
			val correct: Boolean,
			val correctCount: Int,
		) : State

		data class Finished(
			val totalCount: Int,
			val correctCount: Int,
		) : State
	}

	sealed interface Label {

		data object Close : Label
	}
}