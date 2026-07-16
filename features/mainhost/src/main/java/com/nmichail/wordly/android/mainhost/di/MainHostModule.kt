package com.nmichail.wordly.android.mainhost.di

import com.nmichail.wordly.android.mainhost.presentation.DefaultMainHostComponentFactory
import com.nmichail.wordly.android.mainhost.presentation.DefaultRootComponentFactory
import com.nmichail.wordly.android.mainhost.presentation.MainHostComponent
import com.nmichail.wordly.android.mainhost.presentation.RootComponent
import dagger.Binds
import dagger.Module

@Module
abstract class MainHostModule {

	@Binds
	internal abstract fun bindRootComponentFactory(
		impl: DefaultRootComponentFactory,
	): RootComponent.Factory

	@Binds
	internal abstract fun bindMainHostComponentFactory(
		impl: DefaultMainHostComponentFactory,
	): MainHostComponent.Factory
}
