package com.nmichail.wordly.android.core.network.dto

data class SignUpRequest(
	val email: String,
	val password: String,
	val firstName: String,
	val lastName: String,
	val englishLevel: String,
)
