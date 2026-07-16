package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultSignInComponentFactory @Inject constructor(
	private val signInStoreFactory: SignInStoreFactory,
) : SignInComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		signInRouter: SignInRouter,
	): SignInComponent =
		DefaultSignInComponent(
			componentContext = componentContext,
			signInStoreFactory = signInStoreFactory,
			signInRouter = signInRouter,
		)
}
