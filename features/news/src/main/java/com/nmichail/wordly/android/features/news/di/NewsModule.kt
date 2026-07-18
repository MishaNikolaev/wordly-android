package com.nmichail.wordly.android.features.news.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.news.data.api.NewsApi
import com.nmichail.wordly.android.features.news.data.repository.NewsRepositoryImpl
import com.nmichail.wordly.android.features.news.domain.repository.NewsRepository
import com.nmichail.wordly.android.features.news.presentation.DefaultNewsDetailComponentFactory
import com.nmichail.wordly.android.features.news.presentation.NewsDetailComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class NewsModule {

	@Binds
	abstract fun bindNewsRepository(
		impl: NewsRepositoryImpl,
	): NewsRepository

	@Binds
	internal abstract fun bindNewsDetailComponentFactory(
		impl: DefaultNewsDetailComponentFactory,
	): NewsDetailComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideNewsApi(
			@GeneralRetrofit retrofit: Retrofit,
		): NewsApi =
			retrofit.create(NewsApi::class.java)
	}
}
