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

		val authorization = if (token.startsWith(BEARER_PREFIX, ignoreCase = true)) {
			token
		} else {
			"$BEARER_PREFIX$token"
		}

		val request = chain.request()
			.newBuilder()
			.header(TOKEN_HEADER, authorization)
			.build()
		return chain.proceed(request)
	}

	companion object {
		const val TOKEN_HEADER = "Authorization"
		const val BEARER_PREFIX = "Bearer "
	}
}
