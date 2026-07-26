package com.nmichail.wordly.android.features.books.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.books.data.api.BooksApi
import com.nmichail.wordly.android.features.books.data.repository.BooksRepositoryImpl
import com.nmichail.wordly.android.features.books.domain.repository.BooksRepository
import com.nmichail.wordly.android.features.books.presentation.BooksComponent
import com.nmichail.wordly.android.features.books.presentation.DefaultBooksComponentFactory
import com.nmichail.wordly.android.features.books.presentation.detail.BookReaderComponent
import com.nmichail.wordly.android.features.books.presentation.detail.DefaultBookReaderComponentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class BooksModule {

	@Binds
	abstract fun bindBooksRepository(
		impl: BooksRepositoryImpl,
	): BooksRepository

	@Binds
	internal abstract fun bindBooksComponentFactory(
		impl: DefaultBooksComponentFactory,
	): BooksComponent.Factory

	@Binds
	internal abstract fun bindBookReaderComponentFactory(
		impl: DefaultBookReaderComponentFactory,
	): BookReaderComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideBooksApi(
			@GeneralRetrofit retrofit: Retrofit,
		): BooksApi =
			retrofit.create(BooksApi::class.java)
	}
}