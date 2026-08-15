package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.nmichail.wordly.android.core.preferences.domain.usecase.IsAuthTokensExistUseCase
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInRouter
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpRouter
import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionComponent
import com.nmichail.wordly.android.shared.error.presentation.ErrorLogoutRouter
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import javax.inject.Inject

internal class DefaultRootComponent(
	componentContext: ComponentContext,
	private val signInComponentFactory: SignInComponent.Factory,
	private val signUpComponentFactory: SignUpComponent.Factory,
	private val mainHostComponentFactory: MainHostComponent.Factory,
	private val networkSelectionComponentFactory: NetworkSelectionComponent.Factory,
	private val isAuthTokensExistUseCase: IsAuthTokensExistUseCase,
	private val errorLogoutRouter: ErrorLogoutRouter,
) : RootComponent, ComponentContext by componentContext {

	private val navigation = StackNavigation<Config>()

	override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
		source = navigation,
		serializer = Config.serializer(),
		initialStack = {
			listOf(
				if (isAuthTokensExistUseCase()) {
					Config.MainHost
				} else {
					Config.SignIn
				},
			)
		},
		handleBackButton = true,
		childFactory = ::child,
	)

	init {
		errorLogoutRouter.attach {
			navigation.navigate { listOf(Config.SignIn) }
		}
		lifecycle.doOnDestroy {
			errorLogoutRouter.detach()
		}
	}

	@OptIn(DelicateDecomposeApi::class)
	private fun child(
		config: Config,
		componentContext: ComponentContext,
	): RootComponent.Child {
		return when (config) {
			Config.SignIn -> {
				val signInRouter = object : SignInRouter {
					override fun navigateToSignUp() {
						navigation.push(Config.SignUp)
					}

					override fun navigateToMain() {
						navigation.navigate { listOf(Config.MainHost) }
					}

					override fun navigateToNetworkSelection() {
						navigation.push(Config.NetworkSelection)
					}
				}
				val component = signInComponentFactory(
					componentContext = componentContext,
					signInRouter = signInRouter,
				)
				RootComponent.Child.SignIn(component)
			}

			Config.SignUp -> {
				val signUpRouter = object : SignUpRouter {
					override fun navigateToSignIn() {
						navigation.pop()
					}

					override fun navigateToMain() {
						navigation.navigate { listOf(Config.MainHost) }
					}

					override fun openTermsOfUse() {
						// TODO: открыть условия использования по диплинку
					}
				}
				val component = signUpComponentFactory(
					componentContext = componentContext,
					signUpRouter = signUpRouter,
				)
				RootComponent.Child.SignUp(component)
			}

			Config.MainHost -> {
				val component = mainHostComponentFactory(componentContext = componentContext)
				RootComponent.Child.MainHost(component)
			}

			Config.NetworkSelection -> {
				val component = networkSelectionComponentFactory(
					componentContext = componentContext,
					onFinished = navigation::pop,
				)
				RootComponent.Child.NetworkSelection(component)
			}
		}
	}

	@Serializable
	private sealed interface Config {

		@Serializable
		data object SignIn : Config

		@Serializable
		data object SignUp : Config

		@Serializable
		data object MainHost : Config

		@Serializable
		data object NetworkSelection : Config
	}
}

internal class DefaultRootComponentFactory @Inject constructor(
	private val signInComponentFactory: SignInComponent.Factory,
	private val signUpComponentFactory: SignUpComponent.Factory,
	private val mainHostComponentFactory: MainHostComponent.Factory,
	private val networkSelectionComponentFactory: NetworkSelectionComponent.Factory,
	private val isAuthTokensExistUseCase: IsAuthTokensExistUseCase,
	private val errorLogoutRouter: ErrorLogoutRouter,
) : RootComponent.Factory {

	override fun invoke(componentContext: ComponentContext): RootComponent =
		DefaultRootComponent(
			componentContext = componentContext,
			signInComponentFactory = signInComponentFactory,
			signUpComponentFactory = signUpComponentFactory,
			mainHostComponentFactory = mainHostComponentFactory,
			networkSelectionComponentFactory = networkSelectionComponentFactory,
			isAuthTokensExistUseCase = isAuthTokensExistUseCase,
			errorLogoutRouter = errorLogoutRouter,
		)
}
