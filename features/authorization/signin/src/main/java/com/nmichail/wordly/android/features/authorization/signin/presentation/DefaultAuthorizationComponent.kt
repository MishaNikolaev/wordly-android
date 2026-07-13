package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.authorization.signup.presentation.DefaultSignUpComponent
import com.nmichail.wordly.android.shared.authorization.contract.AuthorizationComponent
import com.nmichail.wordly.android.shared.authorization.contract.AuthorizationComponentFactory
import com.nmichail.wordly.android.shared.authorization.contract.AuthorizationConfig

class DefaultAuthorizationComponent(
	componentContext: ComponentContext,
) : ComponentContext by componentContext, AuthorizationComponent {

	private val navigation = StackNavigation<AuthorizationConfig>()

	override val childStack: Value<ChildStack<AuthorizationConfig, AuthorizationComponent.Child>> =
		childStack(
			source = navigation,
			initialStack = { listOf(AuthorizationConfig.SignIn) },
			saveStack = { null },
			restoreStack = { null },
			handleBackButton = true,
			childFactory = ::child,
		)

	private fun child(
		config: AuthorizationConfig,
		componentContext: ComponentContext,
	): AuthorizationComponent.Child =
		when (config) {
			AuthorizationConfig.SignIn -> AuthorizationComponent.Child.SignIn(
				DefaultSignInComponent(
					componentContext = componentContext,
					onSignUpRequested = { navigation.push(AuthorizationConfig.SignUp) },
				),
			)
			AuthorizationConfig.SignUp -> AuthorizationComponent.Child.SignUp(
				DefaultSignUpComponent(
					componentContext = componentContext,
					onSignInRequested = { navigation.pop() },
				),
			)
		}
}

class DefaultAuthorizationComponentFactory : AuthorizationComponentFactory {

	override fun create(componentContext: ComponentContext): AuthorizationComponent =
		DefaultAuthorizationComponent(componentContext)
}
