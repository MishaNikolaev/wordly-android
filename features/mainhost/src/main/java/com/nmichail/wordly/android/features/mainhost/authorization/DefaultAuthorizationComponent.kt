package com.nmichail.wordly.android.features.mainhost.authorization

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
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
		@Suppress("UNUSED_PARAMETER") componentContext: ComponentContext,
	): AuthorizationComponent.Child =
		when (config) {
			AuthorizationConfig.SignIn -> AuthorizationComponent.Child.SignIn
			AuthorizationConfig.SignUp -> AuthorizationComponent.Child.SignUp
		}
}
