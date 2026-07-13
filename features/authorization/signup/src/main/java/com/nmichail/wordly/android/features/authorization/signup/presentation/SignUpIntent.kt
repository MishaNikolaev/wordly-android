package com.nmichail.wordly.android.features.authorization.signup.presentation

sealed interface SignUpIntent {

	data object Submit : SignUpIntent
}
