package com.nmichail.wordly.android.core.preferences.di

import com.google.gson.Gson
import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStoreImpl
import com.nmichail.wordly.android.core.preferences.data.repository.ThemeRepositoryImpl
import com.nmichail.wordly.android.core.preferences.data.repository.TokenRepositoryImpl
import com.nmichail.wordly.android.core.preferences.domain.repository.ThemeRepository
import com.nmichail.wordly.android.core.preferences.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class PreferencesModule {

	@Binds
	abstract fun bindTokenRepository(
		impl: TokenRepositoryImpl,
	): TokenRepository

	@Binds
	@Singleton
	abstract fun bindThemeRepository(
		impl: ThemeRepositoryImpl,
	): ThemeRepository

	@Binds
	@Singleton
	abstract fun bindJsonCacheStore(
		impl: JsonCacheStoreImpl,
	): JsonCacheStore

	companion object {

		@Provides
		@Singleton
		fun provideGson(): Gson = Gson()
	}
}