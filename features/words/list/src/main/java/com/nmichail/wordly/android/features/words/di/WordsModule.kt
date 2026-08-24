package com.nmichail.wordly.android.features.words.di

import com.nmichail.wordly.android.core.network.di.DictionaryRetrofit
import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.core.network.factory.OkHttpClientFactory
import com.nmichail.wordly.android.core.network.factory.RetrofitFactory
import com.nmichail.wordly.android.features.words.data.api.FreeDictionaryApi
import com.nmichail.wordly.android.features.words.data.api.MyMemoryApi
import com.nmichail.wordly.android.features.words.data.api.WordsApi
import com.nmichail.wordly.android.features.words.data.datasource.WordsDataSource
import com.nmichail.wordly.android.features.words.data.datasource.WordsDataSourceImpl
import com.nmichail.wordly.android.features.words.data.repository.WordsRepositoryImpl
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import com.nmichail.wordly.android.features.words.presentation.DefaultWordsComponentFactory
import com.nmichail.wordly.android.features.words.presentation.WordsComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyMemoryRetrofit

@Module
abstract class WordsModule {

	@Binds
	abstract fun bindWordsDataSource(
		impl: WordsDataSourceImpl,
	): WordsDataSource

	@Binds
	@Singleton
	abstract fun bindWordsRepository(
		impl: WordsRepositoryImpl,
	): WordsRepository

	@Binds
	internal abstract fun bindWordsComponentFactory(
		impl: DefaultWordsComponentFactory,
	): WordsComponent.Factory

	companion object {

		private const val DICTIONARY_BASE_URL = "https://api.dictionaryapi.dev/"
		private const val MY_MEMORY_BASE_URL = "https://api.mymemory.translated.net/"

		@Provides
		@Singleton
		@DictionaryRetrofit
		fun provideDictionaryRetrofit(): Retrofit =
			RetrofitFactory.create(
				okHttpClient = OkHttpClientFactory.create(),
				baseUrl = DICTIONARY_BASE_URL,
			)

		@Provides
		@Singleton
		@MyMemoryRetrofit
		fun provideMyMemoryRetrofit(): Retrofit =
			RetrofitFactory.create(
				okHttpClient = OkHttpClientFactory.create(),
				baseUrl = MY_MEMORY_BASE_URL,
			)

		@Provides
		@Singleton
		fun provideFreeDictionaryApi(
			@DictionaryRetrofit retrofit: Retrofit,
		): FreeDictionaryApi =
			retrofit.create(FreeDictionaryApi::class.java)

		@Provides
		@Singleton
		fun provideMyMemoryApi(
			@MyMemoryRetrofit retrofit: Retrofit,
		): MyMemoryApi =
			retrofit.create(MyMemoryApi::class.java)

		@Provides
		@Singleton
		fun provideWordsApi(
			@GeneralRetrofit retrofit: Retrofit,
		): WordsApi =
			retrofit.create(WordsApi::class.java)
	}
}