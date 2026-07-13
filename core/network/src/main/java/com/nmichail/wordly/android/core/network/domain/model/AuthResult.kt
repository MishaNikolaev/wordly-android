package com.nmichail.wordly.android.core.network.domain.model

import com.nmichail.wordly.android.core.network.domain.entity.AuthTokens

sealed interface AuthResult {

	data class Success(val tokens: AuthTokens) : AuthResult

	data object InvalidCredentials : AuthResult

	data object Error : AuthResult
}
