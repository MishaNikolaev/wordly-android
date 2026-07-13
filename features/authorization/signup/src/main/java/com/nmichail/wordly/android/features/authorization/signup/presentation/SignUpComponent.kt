package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

interface SignUpComponent {

	val model: Value<SignUpStore.State>

	val errors: Flow<SignUpError>

	fun handleChangeEmail(email: String)

	fun handleChangePassword(password: String)

	fun handleChangeFirstName(firstName: String)

	fun handleChangeLastName(lastName: String)

	fun handleChangeEnglishLevel(englishLevel: String)

	fun handleSubmit()

	fun handleNavigateToSignIn()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			signUpRouter: SignUpRouter,
		): SignUpComponent
	}
}
