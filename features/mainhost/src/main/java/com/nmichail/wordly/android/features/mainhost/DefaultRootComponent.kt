package com.nmichail.wordly.android.features.mainhost

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.component.contract.RootConfig
import com.nmichail.wordly.android.features.mainhost.authorization.DefaultAuthorizationComponent

class DefaultRootComponent(
	componentContext: ComponentContext,
) : ComponentContext by componentContext, RootComponent {

	private val navigation = StackNavigation<RootConfig>()

	override val childStack: Value<ChildStack<RootConfig, RootComponent.Child>> =
		childStack(
			source = navigation,
			initialStack = { listOf(RootConfig.Authorization) },
			saveStack = { null },
			restoreStack = { null },
			handleBackButton = true,
			childFactory = ::child,
		)

	private fun child(
		config: RootConfig,
		componentContext: ComponentContext,
	): RootComponent.Child =
		when (config) {
			RootConfig.Authorization -> RootComponent.Child.Authorization(
				DefaultAuthorizationComponent(componentContext),
			)
		}
}