package com.nmichail.wordly.android.features.mainhost

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.component.contract.RootConfig
import com.nmichail.wordly.android.features.mainhost.authorization.AuthorizationComponent

interface RootComponent {

	val childStack: Value<ChildStack<RootConfig, Child>>

	sealed interface Child {

		data class Authorization(val component: AuthorizationComponent) : Child
	}
}

fun interface RootComponentFactory {

	fun create(componentContext: ComponentContext): RootComponent
}