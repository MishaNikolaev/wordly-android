package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import kotlinx.coroutines.channels.ReceiveChannel

interface SignUpComponent {

	val model: Value<State>

	fun labelsChannel(): ReceiveChannel<Label>

	fun handleChangeEmail(email: String)

	fun handleChangePassword(password: String)

	fun handleChangeFirstName(firstName: String)

	fun handleChangeLastName(lastName: String)

	fun handleChangeEnglishLevel(englishLevel: String)

	fun handleSubmit()

	fun handleNavigateToSignIn()

	fun handleOpenTermsOfUse()

	fun handleErrorShown()

	data class State(
		val email: EmailValidationItem = EmailValidationItem(),
		val password: PasswordValidationItem = PasswordValidationItem(),
		val firstName: NameValidationItem = NameValidationItem(namePart = NamePart.NAME),
		val lastName: NameValidationItem = NameValidationItem(namePart = NamePart.SURNAME),
		val englishLevel: NotEmptyValidationItem = NotEmptyValidationItem(),
		val isSubmitting: Boolean = false,
		val error: Error? = null,
	)

	sealed interface Error {

		data object RegistrationFailed : Error

		data object NoConnection : Error
	}

	sealed interface Label {

		data object OpenSignIn : Label

		data object OpenMainHost : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			signUpRouter: SignUpRouter,
		): SignUpComponent
	}
}
