package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem

interface SignInStore :
	Store<SignInStore.Intent, SignInStore.State, SignInStore.Label> {

	sealed interface State {

		data object Loading : State

		data class Content(
			val email: EmailValidationItem,
			val password: PasswordValidationItem,
			val submitting: Boolean,
		) : State

		data class Error(val content: Content) : State
	}

	sealed interface Label {

		data object OpenSignUp : Label

		data object OpenMainHost : Label
	}

	sealed interface Intent {

		data class ChangeEmail(val email: String) : Intent

		data class ChangePassword(val password: String) : Intent

		data object Submit : Intent

		data object NavigateToSignUp : Intent

		data object Retry : Intent
	}
}