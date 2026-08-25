package com.nmichail.wordly.android.mainhost.di

import com.nmichail.wordly.android.mainhost.presentation.DefaultMainHostComponent
import com.nmichail.wordly.android.mainhost.presentation.DefaultRootComponent
import com.nmichail.wordly.android.mainhost.presentation.MainHostComponent
import com.nmichail.wordly.android.mainhost.presentation.RootComponent
import dagger.Binds
import dagger.Module

@Module
abstract class MainHostModule {

	@Binds
	internal abstract fun bindRootComponentFactory(
		impl: DefaultRootComponent.Factory,
	): RootComponent.Factory

	@Binds
	internal abstract fun bindMainHostComponentFactory(
		impl: DefaultMainHostComponent.Factory,
	): MainHostComponent.Factory
}
