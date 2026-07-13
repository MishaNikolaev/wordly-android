package com.nmichail.wordly.android.features.authorization.signin.presentation

sealed interface SignInIntent {

	data object Submit : SignInIntent
}
