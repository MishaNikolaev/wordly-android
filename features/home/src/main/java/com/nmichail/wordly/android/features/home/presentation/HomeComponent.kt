package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.home.domain.entity.Training

interface HomeComponent {

	val model: Value<HomeStore.State>

	fun handleRetry()

	fun handleStartReview()

	fun handleOpenTraining(training: Training)

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			homeRouter: HomeRouter,
		): HomeComponent
	}
}
