package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import kotlinx.coroutines.channels.ReceiveChannel

internal class DefaultSignUpComponent(
	componentContext: ComponentContext,
	private val signUpStoreFactory: SignUpStoreFactory,
	private val signUpRouter: SignUpRouter,
) : ComponentContext by componentContext,
	SignUpComponent {

	private val store: SignUpStore = instanceKeeper.getStore {
		signUpStoreFactory.create()
	}

	override val model: Value<SignUpStore.State> = store.asValue()

	override fun labelsChannel(): ReceiveChannel<SignUpStore.Label> =
		store.labelsChannel(lifecycle)

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					SignUpStore.Label.OpenSignIn -> signUpRouter.navigateToSignIn()
					SignUpStore.Label.OpenMainHost -> signUpRouter.navigateToMain()
				}
			}
		}
	}

	override fun handleChangeEmail(email: String) {
		store.accept(SignUpStore.Intent.ChangeEmail(email = email))
	}

	override fun handleChangePassword(password: String) {
		store.accept(SignUpStore.Intent.ChangePassword(password = password))
	}

	override fun handleChangeFirstName(firstName: String) {
		store.accept(SignUpStore.Intent.ChangeFirstName(firstName = firstName))
	}

	override fun handleChangeLastName(lastName: String) {
		store.accept(SignUpStore.Intent.ChangeLastName(lastName = lastName))
	}

	override fun handleChangeEnglishLevel(englishLevel: String) {
		store.accept(SignUpStore.Intent.ChangeEnglishLevel(englishLevel = englishLevel))
	}

	override fun handleSubmit() {
		store.accept(SignUpStore.Intent.Submit)
	}

	override fun handleNavigateToSignIn() {
		store.accept(SignUpStore.Intent.NavigateToSignIn)
	}

	override fun handleOpenTermsOfUse() {
		signUpRouter.openTermsOfUse()
	}

	override fun handleRetry() {
		store.accept(SignUpStore.Intent.Retry)
	}
}
