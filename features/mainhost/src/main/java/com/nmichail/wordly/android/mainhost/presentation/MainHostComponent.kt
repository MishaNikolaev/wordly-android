package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface MainHostComponent {

	val stack: Value<ChildStack<*, Child>>

	fun handleSelectTab(tab: MainHostTab)

	sealed interface Child {

		data object Home : Child

		data object Words : Child

		data object Stats : Child

		data object Profile : Child
	}

	fun interface Factory {

		operator fun invoke(componentContext: ComponentContext): MainHostComponent
	}
}

enum class MainHostTab {
	Home,
	Words,
	Stats,
	Profile,
}
