package com.nmichail.wordly.android.features.movies.di

import com.nmichail.wordly.android.features.movies.presentation.DefaultMoviesComponent
import com.nmichail.wordly.android.features.movies.presentation.MoviesComponent
import dagger.Binds
import dagger.Module

@Module
abstract class MoviesModule {

	@Binds
	internal abstract fun bindMoviesComponentFactory(
		impl: DefaultMoviesComponent.Factory,
	): MoviesComponent.Factory
}
