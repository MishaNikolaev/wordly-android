package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext

class DefaultSignInComponentFactory : SignInComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		onOpenSignUp: () -> Unit,
	): SignInComponent =
		DefaultSignInComponent(
			componentContext = componentContext,
			onOpenSignUp = onOpenSignUp,
		)
}