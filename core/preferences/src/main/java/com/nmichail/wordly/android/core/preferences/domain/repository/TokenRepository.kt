package com.nmichail.wordly.android.core.preferences.domain.repository

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens

interface TokenRepository {

	fun save(tokens: AuthTokens)

	fun get(): AuthTokens?

	fun getAccessToken(): String?

	fun clear()

	fun exists(): Boolean
}
