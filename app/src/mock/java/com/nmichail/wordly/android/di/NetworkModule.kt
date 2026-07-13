package com.nmichail.wordly.android.di

import android.content.Context
import com.nmichail.wordly.android.core.fakenetwork.FakeServerInterceptor
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
	fun provideFakeServerInterceptor(context: Context): FakeServerInterceptor =
		FakeServerInterceptor(context = context)

	@Provides
	@Singleton
	fun provideOkHttpClient(
		fakeServerInterceptor: FakeServerInterceptor,
	): OkHttpClient =
		OkHttpClientFactory.create(interceptors = listOf(fakeServerInterceptor))

	@Provides
	@Singleton
	@GeneralRetrofit
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
		RetrofitFactory.create(
			okHttpClient = okHttpClient,
			baseUrl = NetworkConfig.MOCK_BASE_URL,
		)
}
