package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

interface SignInComponent {

	val model: Value<SignInStore.State>

	val errors: Flow<SignInError>

	fun handleChangeEmail(email: String)

	fun handleChangePassword(password: String)

	fun handleSubmit()

	fun handleNavigateToSignUp()

	fun handleNavigateToNetworkSelection()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			signInRouter: SignInRouter,
		): SignInComponent
	}
}
