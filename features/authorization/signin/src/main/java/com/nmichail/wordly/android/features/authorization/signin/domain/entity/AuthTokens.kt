package com.nmichail.wordly.android.features.authorization.signin.domain.entity

data class AuthTokens(
	val accessToken: String,
	val refreshToken: String,
)
