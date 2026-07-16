package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import kotlinx.coroutines.channels.ReceiveChannel

internal class DefaultSignInComponent(
	componentContext: ComponentContext,
	private val signInStoreFactory: SignInStoreFactory,
	private val signInRouter: SignInRouter,
) : ComponentContext by componentContext,
	SignInComponent {

	private val store: SignInStore = instanceKeeper.getStore {
		signInStoreFactory.create()
	}

	override val model: Value<SignInComponent.State> = store.asValue()

	override fun labelsChannel(): ReceiveChannel<SignInComponent.Label> =
		store.labelsChannel(lifecycle)

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					SignInComponent.Label.OpenSignUp -> signInRouter.navigateToSignUp()
					SignInComponent.Label.OpenMainHost -> signInRouter.navigateToMain()
					else -> Unit
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleChangeEmail(email: String) {
		store.accept(SignInStore.Intent.ChangeEmail(email = email))
	}

	override fun handleChangePassword(password: String) {
		store.accept(SignInStore.Intent.ChangePassword(password = password))
	}

	override fun handleSubmit() {
		store.accept(SignInStore.Intent.Submit)
	}

	override fun handleNavigateToSignUp() {
		store.accept(SignInStore.Intent.NavigateToSignUp)
	}

	override fun handleNavigateToNetworkSelection() {
		signInRouter.navigateToNetworkSelection()
	}
}
