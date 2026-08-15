package com.nmichail.wordly.android.core.preferences.data.repository

import android.content.Context
import com.google.gson.Gson
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.repository.TokenRepository
import javax.inject.Inject

private const val TOKEN_PREFERENCES = "AUTH_TOKEN_PREFERENCES"
private const val TOKEN_KEY = "AUTH_TOKENS"

class TokenRepositoryImpl @Inject constructor(
	context: Context,
	private val gson: Gson,
) : TokenRepository {

	private val preferences = context.getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE)

	override fun save(tokens: AuthTokens) {
		preferences.edit()
			.putString(TOKEN_KEY, gson.toJson(tokens))
			.apply()
	}

	override fun get(): AuthTokens? {
		val json = preferences.getString(TOKEN_KEY, null) ?: return null
		val tokens = try {
			gson.fromJson(json, AuthTokens::class.java)
		} catch (_: Exception) {
			null
		}
		if (tokens == null) {
			clear()
		}
		return tokens
	}

	override fun getAccessToken(): String? =
		get()?.accessToken

	override fun clear() {
		preferences.edit()
			.remove(TOKEN_KEY)
			.apply()
	}

	override fun exists(): Boolean =
		preferences.contains(TOKEN_KEY)
}
