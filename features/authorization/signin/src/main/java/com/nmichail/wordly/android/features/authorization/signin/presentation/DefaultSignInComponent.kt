package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSignInComponent(
	componentContext: ComponentContext,
	private val onOpenSignUp: () -> Unit,
	private val onOpenMainHost: () -> Unit,
) : ComponentContext by componentContext,
	SignInComponent {

	private val store: SignInStore = instanceKeeper.getStore {
		SignInStoreFactory().create()
	}

	init {
		componentScope().launch {
			store.labels.collect { label ->
				when (label) {
					SignInStore.Label.OpenSignUp -> onOpenSignUp()
					SignInStore.Label.OpenMainHost -> onOpenMainHost()
				}
			}
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val model: StateFlow<SignInStore.State>
		get() = store.stateFlow

	override fun onEmailChanged(email: String) {
		store.accept(SignInStore.Intent.ChangeEmail(email = email))
	}

	override fun onPasswordChanged(password: String) {
		store.accept(SignInStore.Intent.ChangePassword(password = password))
	}

	override fun onSubmitClicked() {
		store.accept(SignInStore.Intent.Submit)
	}

	override fun onSignUpClicked() {
		store.accept(SignInStore.Intent.NavigateToSignUp)
	}
}