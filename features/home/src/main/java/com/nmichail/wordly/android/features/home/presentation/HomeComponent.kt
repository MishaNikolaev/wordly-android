package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface HomeComponent {

	val model: Value<HomeStore.State>

	fun handleRetry()

	fun handleRefresh()

	fun handleStartReview()

	fun handleOpenCards()

	fun handleOpenConstructor()

	fun handleOpenBooks()

	fun handleOpenMovies()

	fun handleOpenRecap()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			homeRouter: HomeRouter,
		): HomeComponent
	}
}
