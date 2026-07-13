package com.nmichail.wordly.android.features.authorization.signup.domain.model

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens

sealed interface SignUpResult {

	data class Success(val tokens: AuthTokens) : SignUpResult

	data object InvalidCredentials : SignUpResult

	data object Error : SignUpResult
}
