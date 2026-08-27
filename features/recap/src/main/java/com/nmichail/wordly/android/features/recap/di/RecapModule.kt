package com.nmichail.wordly.android.features.recap.di

import com.nmichail.wordly.android.features.recap.presentation.DefaultRecapComponent
import com.nmichail.wordly.android.features.recap.presentation.RecapComponent
import dagger.Binds
import dagger.Module

@Module
abstract class RecapModule {

	@Binds
	internal abstract fun bindRecapComponentFactory(
		impl: DefaultRecapComponent.Factory,
	): RecapComponent.Factory
}