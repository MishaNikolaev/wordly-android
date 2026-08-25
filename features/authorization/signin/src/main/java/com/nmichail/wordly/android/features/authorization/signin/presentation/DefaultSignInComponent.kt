package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.core.navigation.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

internal class DefaultSignInComponent @AssistedInject constructor(
	private val signInStoreFactory: SignInStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("signInRouter") private val signInRouter: SignInRouter,
) : ComponentContext by componentContext,
	SignInComponent {

	private val store: SignInStore = instanceKeeper.getStore {
		signInStoreFactory.create()
	}

	override val model: Value<SignInStore.State> = store.asValue()

	override fun labelsChannel(): ReceiveChannel<SignInStore.Label> =
		store.labelsChannel(lifecycle)

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					SignInStore.Label.OpenSignUp -> signInRouter.navigateToSignUp()
					SignInStore.Label.OpenMainHost -> signInRouter.navigateToMain()
				}
			}
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
		val content = store.state as? SignInStore.State.Content ?: return
		if (content.submitting) return
		signInRouter.navigateToNetworkSelection()
	}

	override fun handleRetry() {
		store.accept(SignInStore.Intent.Retry)
	}

	@AssistedFactory
	fun interface Factory : SignInComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("signInRouter") signInRouter: SignInRouter,
		): DefaultSignInComponent
	}
}