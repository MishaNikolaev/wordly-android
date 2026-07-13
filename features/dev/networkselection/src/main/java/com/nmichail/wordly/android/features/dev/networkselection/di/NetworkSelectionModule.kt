package com.nmichail.wordly.android.features.dev.networkselection.di

import com.nmichail.wordly.android.features.dev.networkselection.data.repository.EndpointRepositoryImpl
import com.nmichail.wordly.android.features.dev.networkselection.data.repository.MockRepositoryImpl
import com.nmichail.wordly.android.features.dev.networkselection.data.repository.NetworkStandRepositoryImpl
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.EndpointRepository
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.MockRepository
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.NetworkStandRepository
import com.nmichail.wordly.android.features.dev.networkselection.presentation.DefaultNetworkSelectionComponentFactory
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
	abstract fun bindEndpointRepository(
		impl: EndpointRepositoryImpl,
	): EndpointRepository

	@Binds
	abstract fun bindMockRepository(
		impl: MockRepositoryImpl,
	): MockRepository

	@Binds
	abstract fun bindNetworkSelectionComponentFactory(
		impl: DefaultNetworkSelectionComponentFactory,
	): NetworkSelectionComponent.Factory
}
