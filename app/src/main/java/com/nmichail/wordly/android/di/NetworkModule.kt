package com.nmichail.wordly.android.di

import android.content.Context
import com.google.gson.Gson
import com.nmichail.wordly.android.BuildConfig
import com.nmichail.wordly.android.core.fakenetwork.FakeServerInterceptor
import com.nmichail.wordly.android.core.network.config.NetworkConfig
import com.nmichail.wordly.android.core.network.datasource.EndpointDataSource
import com.nmichail.wordly.android.core.network.datasource.EndpointDataSourceImpl
import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.core.network.datasource.MockDataSourceImpl
import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.core.network.factory.OkHttpClientFactory
import com.nmichail.wordly.android.core.network.factory.RetrofitFactory
import com.nmichail.wordly.android.core.network.okhttp.authenticator.AccessTokenRefresher
import com.nmichail.wordly.android.core.network.okhttp.authenticator.TokenAuthenticator
import com.nmichail.wordly.android.core.network.okhttp.interceptor.AuthTokenProvider
import com.nmichail.wordly.android.core.network.okhttp.interceptor.LoggingInterceptor
import com.nmichail.wordly.android.core.network.okhttp.interceptor.TokenInterceptor
import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.repository.TokenRepository
import com.nmichail.wordly.android.core.preferences.domain.usecase.GetAccessTokenUseCase
import dagger.Module
import dagger.Provides
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
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
	fun provideLoggingInterceptor(): LoggingInterceptor =
		LoggingInterceptor()

	@Provides
	@Singleton
	fun provideAccessTokenRefresher(
		tokenRepository: TokenRepository,
		endpointDataSource: EndpointDataSource,
		gson: Gson,
	): AccessTokenRefresher =
		AccessTokenRefresher {
			val refreshToken = tokenRepository.get()?.refreshToken ?: return@AccessTokenRefresher null
			val baseUrl = endpointDataSource.getEndpoint().url.trimEnd('/')
			val client = OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.build()
			val body = gson.toJson(mapOf("refreshToken" to refreshToken))
				.toRequestBody(JSON_MEDIA_TYPE)
			val request = Request.Builder()
				.url("$baseUrl$REFRESH_PATH")
				.post(body)
				.build()
			try {
				client.newCall(request).execute().use { response ->
					if (!response.isSuccessful) {
						tokenRepository.clear()
						return@AccessTokenRefresher null
					}
					val payload = response.body?.string()
					if (payload.isNullOrBlank()) {
						tokenRepository.clear()
						return@AccessTokenRefresher null
					}
					val tokens = gson.fromJson(payload, AuthTokensResponse::class.java)
					if (tokens.accessToken.isBlank() || tokens.refreshToken.isBlank()) {
						tokenRepository.clear()
						return@AccessTokenRefresher null
					}
					tokenRepository.save(
						AuthTokens(
							accessToken = tokens.accessToken,
							refreshToken = tokens.refreshToken,
						),
					)
					tokens.accessToken
				}
			} catch (_: Exception) {
				null
			}
		}

	@Provides
	@Singleton
	fun provideTokenAuthenticator(
		accessTokenRefresher: AccessTokenRefresher,
	): TokenAuthenticator =
		TokenAuthenticator(accessTokenRefresher = accessTokenRefresher)

	@Provides
	@Singleton
	fun provideOkHttpClient(
		mockDataSource: MockDataSource,
		fakeServerInterceptor: FakeServerInterceptor,
		tokenInterceptor: TokenInterceptor,
		loggingInterceptor: LoggingInterceptor,
		tokenAuthenticator: TokenAuthenticator,
	): OkHttpClient {
		val interceptors = buildList {
			add(tokenInterceptor)
			if (BuildConfig.DEBUG) {
				add(loggingInterceptor)
			}
			if (mockDataSource.isMock()) {
				add(fakeServerInterceptor)
			}
		}
		return OkHttpClientFactory.create(
			interceptors = interceptors,
			authenticator = tokenAuthenticator,
		)
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

	private const val REFRESH_PATH = "/api/gateway/refresh"
	private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
}
