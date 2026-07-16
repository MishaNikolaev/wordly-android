package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.mvikotlin.core.store.Store

internal interface SignUpStore :
	Store<SignUpStore.Intent, SignUpComponent.State, SignUpComponent.Label> {

	sealed interface Intent {

		data class ChangeEmail(val email: String) : Intent

		data class ChangePassword(val password: String) : Intent

		data class ChangeFirstName(val firstName: String) : Intent

		data class ChangeLastName(val lastName: String) : Intent

		data class ChangeEnglishLevel(val englishLevel: String) : Intent

		data object Submit : Intent

		data object NavigateToSignIn : Intent
	}
}