package com.nmichail.wordly.android.mainhost.presentation

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class DefaultRootComponent(
	componentContext: ComponentContext,
	private val signInComponentFactory: SignInComponent.Factory,
	private val signUpComponentFactory: SignUpComponent.Factory,
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
				val component = signInComponentFactory(
					componentContext = componentContext,
					onOpenSignUp = { navigation.push(Config.SignUp) },
					onOpenMainHost = { navigation.navigate { listOf(Config.MainHost) } },
				)
				RootComponent.Child.SignIn(component)
			}

			Config.SignUp -> {
				val component = signUpComponentFactory(
					componentContext = componentContext,
					onOpenSignIn = navigation::pop,
				)
				RootComponent.Child.SignUp(component)
			}

			Config.MainHost -> {
				val component = DefaultMainHostComponent(componentContext = componentContext)
				RootComponent.Child.MainHost(component)
			}
		}
	}

	private sealed interface Config : Parcelable {

		@Parcelize
		data object SignIn : Config

		@Parcelize
		data object SignUp : Config

		@Parcelize
		data object MainHost : Config
	}
}

class DefaultRootComponentFactory @Inject constructor(
	private val signInComponentFactory: SignInComponent.Factory,
	private val signUpComponentFactory: SignUpComponent.Factory,
) : RootComponent.Factory {

	override fun invoke(componentContext: ComponentContext): RootComponent =
		DefaultRootComponent(
			componentContext = componentContext,
			signInComponentFactory = signInComponentFactory,
			signUpComponentFactory = signUpComponentFactory,
		)
}
