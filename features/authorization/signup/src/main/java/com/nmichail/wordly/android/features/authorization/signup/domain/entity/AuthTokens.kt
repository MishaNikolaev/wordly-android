package com.nmichail.wordly.android.features.authorization.signup.domain.entity

data class AuthTokens(
	val accessToken: String,
	val refreshToken: String,
)
