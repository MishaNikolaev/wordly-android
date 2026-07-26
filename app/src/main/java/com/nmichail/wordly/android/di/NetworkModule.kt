package com.nmichail.wordly.android.di

import android.content.Context
import com.nmichail.wordly.android.BuildConfig
import com.nmichail.wordly.android.core.fakenetwork.FakeServerInterceptor
import com.nmichail.wordly.android.core.network.api.EnglishLevelApi
import com.nmichail.wordly.android.core.network.config.NetworkConfig
import com.nmichail.wordly.android.core.network.datasource.EndpointDataSource
import com.nmichail.wordly.android.core.network.datasource.EndpointDataSourceImpl
import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.core.network.datasource.MockDataSourceImpl
import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.core.network.factory.OkHttpClientFactory
import com.nmichail.wordly.android.core.network.factory.RetrofitFactory
import com.nmichail.wordly.android.core.network.okhttp.interceptor.AuthTokenProvider
import com.nmichail.wordly.android.core.network.okhttp.interceptor.TokenInterceptor
import com.nmichail.wordly.android.core.preferences.domain.usecase.GetAccessTokenUseCase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object NetworkModule {

	@Provides
	@Singleton
	fun provideEndpointDataSource(context: Context): EndpointDataSource =
		EndpointDataSourceImpl(context = context)

	@Provides
	@Singleton
	fun provideMockDataSource(context: Context): MockDataSource =
		MockDataSourceImpl(
			context = context,
			defaultIsMock = BuildConfig.FLAVOR == "mock",
		)

	@Provides
	@Singleton
	fun provideFakeServerInterceptor(context: Context): FakeServerInterceptor =
		FakeServerInterceptor(context = context)

	@Provides
	@Singleton
	fun provideAuthTokenProvider(
		getAccessTokenUseCase: GetAccessTokenUseCase,
	): AuthTokenProvider =
		AuthTokenProvider { getAccessTokenUseCase() }

	@Provides
	@Singleton
	fun provideTokenInterceptor(
		authTokenProvider: AuthTokenProvider,
	): TokenInterceptor =
		TokenInterceptor(authTokenProvider = authTokenProvider)

	@Provides
	@Singleton
	fun provideOkHttpClient(
		mockDataSource: MockDataSource,
		fakeServerInterceptor: FakeServerInterceptor,
		tokenInterceptor: TokenInterceptor,
	): OkHttpClient {
		val interceptors = buildList {
			add(tokenInterceptor)
			if (mockDataSource.isMock()) {
				add(fakeServerInterceptor)
			}
		}
		return OkHttpClientFactory.create(interceptors = interceptors)
	}

	@Provides
	@Singleton
	@GeneralRetrofit
	fun provideRetrofit(
		okHttpClient: OkHttpClient,
		mockDataSource: MockDataSource,
		endpointDataSource: EndpointDataSource,
	): Retrofit {
		val baseUrl = if (mockDataSource.isMock()) {
			NetworkConfig.MOCK_BASE_URL
		} else {
			endpointDataSource.getEndpoint().url
		}
		return RetrofitFactory.create(
			okHttpClient = okHttpClient,
			baseUrl = baseUrl,
		)
	}

	@Provides
	@Singleton
	fun provideEnglishLevelApi(
		@GeneralRetrofit retrofit: Retrofit,
	): EnglishLevelApi =
		retrofit.create(EnglishLevelApi::class.java)
}
