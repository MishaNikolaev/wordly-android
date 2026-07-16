package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

internal class DefaultMainHostComponent(
	componentContext: ComponentContext,
) : MainHostComponent, ComponentContext by componentContext {

	private val navigation = StackNavigation<MainHostConfig>()

	override val stack: Value<ChildStack<*, MainHostComponent.Child>> = childStack(
		source = navigation,
		serializer = MainHostConfig.serializer(),
		initialStack = { listOf(MainHostConfig.Home) },
		handleBackButton = true,
		childFactory = ::child,
	)

	override fun handleSelectTab(tab: MainHostTab) {
		navigation.bringToFront(tab.toConfig())
	}

	private fun child(
		config: MainHostConfig,
		componentContext: ComponentContext,
	): MainHostComponent.Child =
		when (config) {
			MainHostConfig.Home -> MainHostComponent.Child.Home
			MainHostConfig.Words -> MainHostComponent.Child.Words
			MainHostConfig.Stats -> MainHostComponent.Child.Stats
			MainHostConfig.Profile -> MainHostComponent.Child.Profile
		}
}

@Serializable
private sealed interface MainHostConfig {

	@Serializable
	data object Home : MainHostConfig

	@Serializable
	data object Words : MainHostConfig

	@Serializable
	data object Stats : MainHostConfig

	@Serializable
	data object Profile : MainHostConfig
}

private fun MainHostTab.toConfig(): MainHostConfig =
	when (this) {
		MainHostTab.Home -> MainHostConfig.Home
		MainHostTab.Words -> MainHostConfig.Words
		MainHostTab.Stats -> MainHostConfig.Stats
		MainHostTab.Profile -> MainHostConfig.Profile
	}

fun MainHostComponent.Child.toTab(): MainHostTab =
	when (this) {
		MainHostComponent.Child.Home -> MainHostTab.Home
		MainHostComponent.Child.Words -> MainHostTab.Words
		MainHostComponent.Child.Stats -> MainHostTab.Stats
		MainHostComponent.Child.Profile -> MainHostTab.Profile
	}
