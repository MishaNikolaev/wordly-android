package com.nmichail.wordly.android.features.books.detail.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.books.detail.data.api.BookDetailApi
import com.nmichail.wordly.android.features.books.detail.data.repository.BookDetailRepositoryImpl
import com.nmichail.wordly.android.features.books.detail.domain.repository.BookDetailRepository
import com.nmichail.wordly.android.features.books.detail.presentation.BookDetailComponent
import com.nmichail.wordly.android.features.books.detail.presentation.DefaultBookDetailComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class BookDetailModule {

	@Binds
	abstract fun bindBookDetailRepository(
		impl: BookDetailRepositoryImpl,
	): BookDetailRepository

	@Binds
	internal abstract fun bindBookDetailComponentFactory(
		impl: DefaultBookDetailComponent.Factory,
	): BookDetailComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideBookDetailApi(
			@GeneralRetrofit retrofit: Retrofit,
		): BookDetailApi =
			retrofit.create(BookDetailApi::class.java)
	}
}