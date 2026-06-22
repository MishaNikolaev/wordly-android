package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext

class DefaultSignUpComponentFactory : SignUpComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		onOpenSignIn: () -> Unit,
	): SignUpComponent =
		DefaultSignUpComponent(
			componentContext = componentContext,
			onOpenSignIn = onOpenSignIn,
		)
}
