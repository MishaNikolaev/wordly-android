package com.nmichail.wordly.android.core.network.okhttp.interceptor

import okhttp3.Interceptor
import okhttp3.Response

fun interface AuthTokenProvider {

	fun getAccessToken(): String?
}

class TokenInterceptor(
	private val authTokenProvider: AuthTokenProvider,
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val token = authTokenProvider.getAccessToken()
		if (token.isNullOrBlank()) {
			return chain.proceed(chain.request())
		}

		val request = chain.request()
			.newBuilder()
			.header(TOKEN_HEADER, token)
			.build()
		return chain.proceed(request)
	}

	companion object {
		const val TOKEN_HEADER = "Authorization"
	}
}
