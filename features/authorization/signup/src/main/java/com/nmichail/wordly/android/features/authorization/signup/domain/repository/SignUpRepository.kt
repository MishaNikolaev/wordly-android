package com.nmichail.wordly.android.features.authorization.signup.domain.repository

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm

interface SignUpRepository {

	suspend fun signUp(form: SignUpForm): AuthTokens
}
