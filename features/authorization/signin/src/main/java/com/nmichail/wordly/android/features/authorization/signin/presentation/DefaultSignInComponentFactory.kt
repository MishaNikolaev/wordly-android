package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

class DefaultSignInComponentFactory @Inject constructor() : SignInComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		onOpenSignUp: () -> Unit,
		onOpenMainHost: () -> Unit,
	): SignInComponent =
		DefaultSignInComponent(
			componentContext = componentContext,
			onOpenSignUp = onOpenSignUp,
			onOpenMainHost = onOpenMainHost,
		)
}