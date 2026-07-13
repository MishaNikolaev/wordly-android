package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionRouter
import com.nmichail.wordly.android.navigation.router.ErrorLogoutRouterImpl
import com.nmichail.wordly.android.navigation.router.NetworkSelectionRouterImpl
import com.nmichail.wordly.android.shared.error.presentation.ErrorLogoutRouter
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RoutersModule {

	@Binds
	@Singleton
	abstract fun bindNetworkSelectionRouter(
		impl: NetworkSelectionRouterImpl,
	): NetworkSelectionRouter

	@Binds
	@Singleton
	abstract fun bindErrorLogoutRouter(
		impl: ErrorLogoutRouterImpl,
	): ErrorLogoutRouter
}
