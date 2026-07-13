package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DefaultSignInComponent(
	componentContext: ComponentContext,
	private val signInStoreFactory: SignInStoreFactory,
	private val signInRouter: SignInRouter,
) : ComponentContext by componentContext,
	SignInComponent {

	private val store: SignInStore = instanceKeeper.getStore {
		signInStoreFactory.create()
	}

	private val errorEvents = MutableSharedFlow<SignInError>(extraBufferCapacity = 1)

	override val model: Value<SignInStore.State> = store.asValue()

	override val errors: Flow<SignInError> = errorEvents.asSharedFlow()

	init {
		launchTry {
			store.labels.collect { label ->
				when (label) {
					SignInStore.Label.OpenSignUp -> signInRouter.navigateToSignUp()
					SignInStore.Label.OpenMainHost -> signInRouter.navigateToMain()
					SignInStore.Label.ShowInvalidCredentials -> errorEvents.emit(SignInError.InvalidCredentials)
					SignInStore.Label.ShowNoConnection -> errorEvents.emit(SignInError.NoConnection)
					SignInStore.Label.ShowUnknownError -> errorEvents.emit(SignInError.Unknown)
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
