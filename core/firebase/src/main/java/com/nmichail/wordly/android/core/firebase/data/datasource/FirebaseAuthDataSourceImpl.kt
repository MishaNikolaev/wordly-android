package com.nmichail.wordly.android.core.firebase.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSourceImpl @Inject constructor(
	private val firebaseAuth: FirebaseAuth,
) : FirebaseAuthDataSource {

	override suspend fun signIn(email: String, password: String): AuthTokens {
		val result = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
		return result.user.toAuthTokens()
	}

	override suspend fun signUp(email: String, password: String): AuthTokens {
		val result = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
		return result.user.toAuthTokens()
	}

	override fun signOut() {
		firebaseAuth.signOut()
	}

	private suspend fun FirebaseUser?.toAuthTokens(): AuthTokens {
		val user = this ?: error("Firebase аккаунт пустой")
		val idToken = user.getIdToken(true).await().token
			?: error("Firebase ID token пустое")
		return AuthTokens(
			accessToken = idToken,
			refreshToken = "firebase",
		)
	}
}