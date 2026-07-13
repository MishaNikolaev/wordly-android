package com.nmichail.wordly.android.features.authorization.signup.data.dto

data class AuthTokensResponse(
	val accessToken: String,
	val refreshToken: String,
)
