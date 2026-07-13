package com.nmichail.wordly.android.features.authorization.signin.domain.model

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens

sealed interface SignInResult {

	data class Success(val tokens: AuthTokens) : SignInResult

	data object InvalidCredentials : SignInResult

	data object Error : SignInResult
}
