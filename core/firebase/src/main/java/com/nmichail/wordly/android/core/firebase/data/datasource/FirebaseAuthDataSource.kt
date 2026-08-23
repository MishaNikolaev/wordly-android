package com.nmichail.wordly.android.core.firebase.data.datasource

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens

interface FirebaseAuthDataSource {

	suspend fun signIn(email: String, password: String): AuthTokens

	suspend fun signUp(email: String, password: String): AuthTokens

	fun signOut()
}