package com.nmichail.wordly.android.features.authorization.signin.data.dto

data class AuthTokensResponse(
	val accessToken: String,
	val refreshToken: String,
)
