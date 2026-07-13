package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.core.network.config.NetworkConfig
import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.core.network.factory.OkHttpClientFactory
import com.nmichail.wordly.android.core.network.factory.RetrofitFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object NetworkModule {

	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient =
		OkHttpClientFactory.create()

	@Provides
	@Singleton
	@GeneralRetrofit
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
		RetrofitFactory.create(
			okHttpClient = okHttpClient,
			baseUrl = NetworkConfig.DEV_BASE_URL,
		)
}
