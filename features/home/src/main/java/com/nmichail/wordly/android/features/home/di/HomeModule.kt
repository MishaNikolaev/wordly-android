package com.nmichail.wordly.android.features.home.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.home.data.api.HomeApi
import com.nmichail.wordly.android.features.home.data.datasource.HomeDataSource
import com.nmichail.wordly.android.features.home.data.datasource.HomeDataSourceImpl
import com.nmichail.wordly.android.features.home.data.repository.HomeRepositoryImpl
import com.nmichail.wordly.android.features.home.domain.repository.HomeRepository
import com.nmichail.wordly.android.features.home.presentation.DefaultHomeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.time.Clock
import javax.inject.Singleton

@Module
abstract class HomeModule {

	@Binds
	abstract fun bindHomeDataSource(
		impl: HomeDataSourceImpl,
	): HomeDataSource

	@Binds
	abstract fun bindHomeRepository(
		impl: HomeRepositoryImpl,
	): HomeRepository

	@Binds
	internal abstract fun bindHomeComponentFactory(
		impl: DefaultHomeComponent.Factory,
	): HomeComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideHomeApi(
			@GeneralRetrofit retrofit: Retrofit,
		): HomeApi =
			retrofit.create(HomeApi::class.java)

		@Provides
		@Singleton
		fun provideClock(): Clock =
			Clock.systemDefaultZone()
	}
}