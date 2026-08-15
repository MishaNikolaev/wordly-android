package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.channels.ReceiveChannel

interface SignUpComponent {

	val model: Value<SignUpStore.State>

	fun labelsChannel(): ReceiveChannel<SignUpStore.Label>

	fun handleChangeEmail(email: String)

	fun handleChangePassword(password: String)

	fun handleChangeFirstName(firstName: String)

	fun handleChangeLastName(lastName: String)

	fun handleChangeEnglishLevel(englishLevel: String)

	fun handleSubmit()

	fun handleNavigateToSignIn()

	fun handleOpenTermsOfUse()

	fun handleRetry()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			signUpRouter: SignUpRouter,
		): SignUpComponent
	}
}
