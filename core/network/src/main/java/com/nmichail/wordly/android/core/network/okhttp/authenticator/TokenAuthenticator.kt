package com.nmichail.wordly.android.core.network.okhttp.authenticator

import com.nmichail.wordly.android.core.network.config.HTTP_UNAUTHORIZED
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

fun interface AccessTokenRefresher {

	fun refreshAccessToken(): String?
}

/**
 * On 401, tries to refresh the access token once and retries the original request.
 */
class TokenAuthenticator(
	private val accessTokenRefresher: AccessTokenRefresher,
) : Authenticator {

	override fun authenticate(route: Route?, response: Response): Request? {
		if (response.code != HTTP_UNAUTHORIZED) {
			return null
		}
		if (responseCount(response) >= MAX_RETRY_COUNT) {
			return null
		}
		if (response.request.url.encodedPath.endsWith(REFRESH_PATH_SUFFIX)) {
			return null
		}

		val newAccessToken = synchronized(refreshLock) {
			accessTokenRefresher.refreshAccessToken()
		} ?: return null

		return response.request.newBuilder()
			.header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$newAccessToken")
			.build()
	}

	private fun responseCount(response: Response): Int {
		var current: Response? = response
		var count = 1
		while (current?.priorResponse != null) {
			count++
			current = current.priorResponse
		}
		return count
	}

	private companion object {
		const val MAX_RETRY_COUNT = 2
		const val REFRESH_PATH_SUFFIX = "/api/gateway/refresh"
		const val AUTHORIZATION_HEADER = "Authorization"
		const val BEARER_PREFIX = "Bearer "
		val refreshLock = Any()
	}
}
