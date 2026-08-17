package com.nmichail.wordly.android.features.authorization.signin.domain.repository

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData

interface SignInRepository {

    suspend fun signIn(signInData: SignInData): AuthTokens
}