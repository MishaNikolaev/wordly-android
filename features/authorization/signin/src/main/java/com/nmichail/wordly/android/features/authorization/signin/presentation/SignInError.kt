package com.nmichail.wordly.android.features.authorization.signin.presentation

sealed interface SignInError {

	data object InvalidCredentials : SignInError

	data object NoConnection : SignInError

	data object Unknown : SignInError
}
