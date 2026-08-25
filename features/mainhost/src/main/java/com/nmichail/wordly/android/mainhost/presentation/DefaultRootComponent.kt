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
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.core.preferences.domain.usecase.IsAuthTokensExistUseCase
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInRouter
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpRouter
import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionComponent
import com.nmichail.wordly.android.features.profile.domain.usecase.GetSessionUseCase
import com.nmichail.wordly.android.shared.error.presentation.ErrorLogoutRouter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

internal class DefaultRootComponent @AssistedInject constructor(
	private val signInComponentFactory: SignInComponent.Factory,
	private val signUpComponentFactory: SignUpComponent.Factory,
	private val mainHostComponentFactory: MainHostComponent.Factory,
	private val networkSelectionComponentFactory: NetworkSelectionComponent.Factory,
	private val isAuthTokensExistUseCase: IsAuthTokensExistUseCase,
	private val getSessionUseCase: GetSessionUseCase,
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase,
	private val errorLogoutRouter: ErrorLogoutRouter,
	@Assisted("componentContext") componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

	private val navigation = StackNavigation<Config>()
	private val scope = coroutineScope()

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
		validateSessionIfNeeded()
		errorLogoutRouter.attach {
			navigation.navigate { listOf(Config.SignIn) }
		}
		lifecycle.doOnDestroy {
			errorLogoutRouter.detach()
		}
	}

	/** Like cft_shift splash: local tokens are not enough — confirm session with backend. */
	private fun validateSessionIfNeeded() {
		if (!isAuthTokensExistUseCase()) return
		scope.launch {
			try {
				getSessionUseCase()
			} catch (_: Exception) {
				clearAuthTokensUseCase()
				navigation.navigate { listOf(Config.SignIn) }
			}
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
				val component = mainHostComponentFactory(
					componentContext = componentContext,
					onOpenNetworkSelection = {
						navigation.push(Config.NetworkSelection)
					},
				)
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

	@AssistedFactory
	fun interface Factory : RootComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
		): DefaultRootComponent
	}
}
