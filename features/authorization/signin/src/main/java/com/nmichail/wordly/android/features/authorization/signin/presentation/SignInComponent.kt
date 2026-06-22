package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface SignInComponent {

	val model: StateFlow<SignInStore.State>

	fun onSubmitClicked()

	fun onSignUpClicked()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onOpenSignUp: () -> Unit,
		): SignInComponent
	}
}