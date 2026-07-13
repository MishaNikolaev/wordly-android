package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem

interface SignInStore : Store<SignInStore.Intent, SignInStore.State, SignInStore.Label> {

	data class State(
		val email: EmailValidationItem = EmailValidationItem(),
		val password: PasswordValidationItem = PasswordValidationItem(),
	)

	sealed interface Intent {

		data class ChangeEmail(val email: String) : Intent

		data class ChangePassword(val password: String) : Intent

		data object Submit : Intent

		data object NavigateToSignUp : Intent
	}

	sealed interface Label {

		data object OpenSignUp : Label

		data object OpenMainHost : Label

		data object ShowInvalidCredentials : Label

		data object ShowNoConnection : Label

		data object ShowUnknownError : Label
	}
}
