package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import kotlinx.coroutines.channels.ReceiveChannel

interface SignInComponent {

	val model: Value<State>

	fun labelsChannel(): ReceiveChannel<Label>

	fun handleChangeEmail(email: String)

	fun handleChangePassword(password: String)

	fun handleSubmit()

	fun handleNavigateToSignUp()

	fun handleNavigateToNetworkSelection()

	data class State(
		val email: EmailValidationItem = EmailValidationItem(),
		val password: PasswordValidationItem = PasswordValidationItem(),
	)

	sealed interface Label {

		data object OpenSignUp : Label

		data object OpenMainHost : Label

		data object ShowInvalidCredentials : Label

		data object ShowNoConnection : Label

		data object ShowUnknownError : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			signInRouter: SignInRouter,
		): SignInComponent
	}
}
