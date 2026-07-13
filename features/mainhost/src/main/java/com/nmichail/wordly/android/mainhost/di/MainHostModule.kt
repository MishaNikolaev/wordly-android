package com.nmichail.wordly.android.mainhost.di

import com.nmichail.wordly.android.mainhost.presentation.DefaultRootComponentFactory
import com.nmichail.wordly.android.mainhost.presentation.RootComponent
import dagger.Binds
import dagger.Module

@Module
abstract class MainHostModule {

	@Binds
	abstract fun bindRootComponentFactory(
		impl: DefaultRootComponentFactory,
	): RootComponent.Factory
}
