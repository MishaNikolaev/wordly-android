package com.nmichail.wordly.android.features.dev.networkselection.di

import com.nmichail.wordly.android.features.dev.networkselection.data.repository.NetworkStandRepositoryImpl
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.NetworkStandRepository
import com.nmichail.wordly.android.features.dev.networkselection.presentation.DefaultNetworkSelectionComponent
import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionComponent
import dagger.Binds
import dagger.Module

@Module
abstract class NetworkSelectionModule {

	@Binds
	abstract fun bindNetworkStandRepository(
		impl: NetworkStandRepositoryImpl,
	): NetworkStandRepository

	@Binds
	internal abstract fun bindNetworkSelectionComponentFactory(
		impl: DefaultNetworkSelectionComponent.Factory,
	): NetworkSelectionComponent.Factory
}
