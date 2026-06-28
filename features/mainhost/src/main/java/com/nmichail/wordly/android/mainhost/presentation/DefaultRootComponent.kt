package com.nmichail.wordly.android.mainhost.presentation

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.authorization.signin.presentation.DefaultSignInComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.DefaultSignUpComponent
import kotlinx.parcelize.Parcelize

class DefaultRootComponent(
	componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

	private val navigation = StackNavigation<Config>()

	override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
		source = navigation,
		initialStack = { listOf(Config.SignIn) },
		saveStack = { null },
		restoreStack = { null },
		handleBackButton = true,
		childFactory = ::child,
	)

	private fun child(
		config: Config,
		componentContext: ComponentContext,
	): RootComponent.Child {
		return when (config) {
			Config.SignIn -> {
				val component = DefaultSignInComponent(
					componentContext = componentContext,
					onOpenSignUp = { navigation.push(Config.SignUp) },
				)
				RootComponent.Child.SignIn(component)
			}

			Config.SignUp -> {
				val component = DefaultSignUpComponent(
					componentContext = componentContext,
					onOpenSignIn = navigation::pop,
				)
				RootComponent.Child.SignUp(component)
			}
		}
	}

	private sealed interface Config : Parcelable {

		@Parcelize
		data object SignIn : Config

		@Parcelize
		data object SignUp : Config
	}
}

class DefaultRootComponentFactory : RootComponent.Factory {

	override fun invoke(componentContext: ComponentContext): RootComponent =
		DefaultRootComponent(componentContext = componentContext)
}