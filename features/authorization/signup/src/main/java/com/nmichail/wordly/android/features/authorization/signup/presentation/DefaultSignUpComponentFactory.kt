package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

class DefaultSignUpComponentFactory @Inject constructor() : SignUpComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		onOpenSignIn: () -> Unit,
	): SignUpComponent =
		DefaultSignUpComponent(
			componentContext = componentContext,
			onOpenSignIn = onOpenSignIn,
		)
}
