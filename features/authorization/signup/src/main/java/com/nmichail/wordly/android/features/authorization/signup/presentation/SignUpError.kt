package com.nmichail.wordly.android.features.authorization.signup.presentation

sealed interface SignUpError {

	data object RegistrationError : SignUpError

	data object NoConnection : SignUpError

	data object Unknown : SignUpError
}
