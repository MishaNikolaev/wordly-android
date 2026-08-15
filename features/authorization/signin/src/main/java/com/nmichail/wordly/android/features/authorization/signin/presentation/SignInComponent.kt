package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.channels.ReceiveChannel

interface SignInComponent {

	val model: Value<SignInStore.State>

	fun labelsChannel(): ReceiveChannel<SignInStore.Label>

	fun handleChangeEmail(email: String)

	fun handleChangePassword(password: String)

	fun handleSubmit()

	fun handleNavigateToSignUp()

	fun handleNavigateToNetworkSelection()

	fun handleRetry()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			signInRouter: SignInRouter,
		): SignInComponent
	}
}
