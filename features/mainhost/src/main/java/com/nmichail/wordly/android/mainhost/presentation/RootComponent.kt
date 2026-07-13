package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionComponent

interface RootComponent {

	val stack: Value<ChildStack<*, Child>>

	sealed interface Child {

		class SignIn(val component: SignInComponent) : Child

		class SignUp(val component: SignUpComponent) : Child

		class MainHost(val component: MainHostComponent) : Child

		class NetworkSelection(val component: NetworkSelectionComponent) : Child
	}

	fun interface Factory {

		operator fun invoke(componentContext: ComponentContext): RootComponent
	}
}
