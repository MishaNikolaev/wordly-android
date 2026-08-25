package com.nmichail.wordly.android.features.books.reader.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.books.reader.data.api.BookReaderApi
import com.nmichail.wordly.android.features.books.reader.data.datasource.BookReaderDataSource
import com.nmichail.wordly.android.features.books.reader.data.datasource.BookReaderDataSourceImpl
import com.nmichail.wordly.android.features.books.reader.data.repository.BookReaderRepositoryImpl
import com.nmichail.wordly.android.features.books.reader.domain.repository.BookReaderRepository
import com.nmichail.wordly.android.features.books.reader.presentation.BookReaderComponent
import com.nmichail.wordly.android.features.books.reader.presentation.DefaultBookReaderComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class BookReaderModule {

    @Binds
    abstract fun bindBookReaderDataSource(
        impl: BookReaderDataSourceImpl,
    ): BookReaderDataSource

    @Binds
    abstract fun bindBookReaderRepository(
        impl: BookReaderRepositoryImpl,
    ): BookReaderRepository

    @Binds
    internal abstract fun bindBookReaderComponentFactory(
        impl: DefaultBookReaderComponent.Factory,
    ): BookReaderComponent.Factory

    companion object {

        @Provides
        @Singleton
        fun provideBookReaderApi(
            @GeneralRetrofit retrofit: Retrofit,
        ): BookReaderApi =
            retrofit.create(BookReaderApi::class.java)
    }
}
