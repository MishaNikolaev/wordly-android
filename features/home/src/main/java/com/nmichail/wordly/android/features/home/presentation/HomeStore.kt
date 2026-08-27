package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.home.domain.entity.Training

interface HomeStore :
	Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Loading : State

		data class Content(
			val firstName: String,
			val streakDays: Int,
			val wordsToReview: Int,
			val estimatedMinutes: Int,
			val reviewStreakDays: Int,
			val trainings: List<Training>,
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object StartReview : Label

		data object OpenCards : Label

		data object OpenConstructor : Label

		data object OpenBooks : Label

		data object OpenMovies : Label

		data object OpenRecap : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data object StartReview : Intent

		data object OpenCards : Intent

		data object OpenConstructor : Intent

		data object OpenBooks : Intent

		data object OpenMovies : Intent

		data object OpenRecap : Intent
	}
}
