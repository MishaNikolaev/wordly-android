package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSignUpComponent(
	componentContext: ComponentContext,
	private val onOpenSignIn: () -> Unit,
) : ComponentContext by componentContext,
	SignUpComponent {

	private val store: SignUpStore = instanceKeeper.getStore {
		SignUpStoreFactory().create()
	}

	init {
		componentScope().launch {
			store.labels.collect { label ->
				when (label) {
					SignUpStore.Label.OpenSignIn -> onOpenSignIn()
				}
			}
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val model: StateFlow<SignUpStore.State>
		get() = store.stateFlow

	override fun onSubmitClicked() {
		store.accept(SignUpStore.Intent.Submit)
	}

	override fun onSignInClicked() {
		store.accept(SignUpStore.Intent.NavigateToSignIn)
	}
}