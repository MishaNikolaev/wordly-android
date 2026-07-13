package com.nmichail.wordly.android.core.network.domain.repository

import com.nmichail.wordly.android.core.network.domain.model.AuthResult

interface AuthRepository {

	suspend fun signIn(email: String, password: String): AuthResult

	suspend fun signUp(
		email: String,
		password: String,
		firstName: String,
		lastName: String,
		englishLevel: String,
	): AuthResult
}
