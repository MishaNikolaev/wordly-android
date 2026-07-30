package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.home.domain.entity.Training

internal interface HomeStore :
	Store<HomeStore.Intent, HomeComponent.State, HomeComponent.Label> {

	sealed interface Intent {

		data object Retry : Intent

		data object OpenMonth : Intent

		data object DismissMonth : Intent

		data object PreviousMonth : Intent

		data object NextMonth : Intent

		data object GoToCurrentMonth : Intent

		data object StartReview : Intent

		data class OpenTraining(val training: Training) : Intent
	}
}
