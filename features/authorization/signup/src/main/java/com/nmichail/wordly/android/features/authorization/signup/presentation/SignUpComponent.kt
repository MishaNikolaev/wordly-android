package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface SignUpComponent {

	val model: StateFlow<SignUpStore.State>

	fun onSubmitClicked()

	fun onSignInClicked()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onOpenSignIn: () -> Unit,
		): SignUpComponent
	}
}
