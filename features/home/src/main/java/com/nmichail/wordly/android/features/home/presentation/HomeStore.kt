package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.news.domain.entity.News

internal interface HomeStore :
	Store<HomeStore.Intent, HomeComponent.State, HomeComponent.Label> {

	sealed interface Intent {

		data object OpenMonth : Intent

		data object DismissMonth : Intent

		data object PreviousMonth : Intent

		data object NextMonth : Intent

		data object GoToCurrentMonth : Intent

		data object StartReview : Intent

		data class OpenTraining(val training: Training) : Intent

		data class OpenNews(val news: News) : Intent
	}
}
