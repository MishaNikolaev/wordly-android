package com.nmichail.wordly.android.features.movies.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface MoviesComponent {

	val model: Value<MoviesStore.State>

	fun handleBack()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			moviesRouter: MoviesRouter,
		): MoviesComponent
	}
}