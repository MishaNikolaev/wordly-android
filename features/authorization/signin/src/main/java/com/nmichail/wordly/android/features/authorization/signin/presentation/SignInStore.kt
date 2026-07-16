package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.mvikotlin.core.store.Store

internal interface SignInStore :
	Store<SignInStore.Intent, SignInComponent.State, SignInComponent.Label> {

	sealed interface Intent {

		data class ChangeEmail(val email: String) : Intent

		data class ChangePassword(val password: String) : Intent

		data object Submit : Intent

		data object NavigateToSignUp : Intent
	}
}
