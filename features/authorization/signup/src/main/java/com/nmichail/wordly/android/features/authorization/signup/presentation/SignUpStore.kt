package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem

interface SignUpStore : Store<SignUpStore.Intent, SignUpStore.State, SignUpStore.Label> {

	data class State(
		val email: EmailValidationItem = EmailValidationItem(),
		val password: PasswordValidationItem = PasswordValidationItem(),
		val firstName: NameValidationItem = NameValidationItem(namePart = NamePart.NAME),
		val lastName: NameValidationItem = NameValidationItem(namePart = NamePart.SURNAME),
		val englishLevel: NotEmptyValidationItem = NotEmptyValidationItem(),
	)

	sealed interface Intent {

		data class ChangeEmail(val email: String) : Intent

		data class ChangePassword(val password: String) : Intent

		data class ChangeFirstName(val firstName: String) : Intent

		data class ChangeLastName(val lastName: String) : Intent

		data class ChangeEnglishLevel(val englishLevel: String) : Intent

		data object Submit : Intent

		data object NavigateToSignIn : Intent
	}

	sealed interface Label {

		data object OpenSignIn : Label
	}
}