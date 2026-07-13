package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DefaultSignUpComponent(
	componentContext: ComponentContext,
	private val signUpStoreFactory: SignUpStoreFactory,
	private val signUpRouter: SignUpRouter,
) : ComponentContext by componentContext,
	SignUpComponent {

	private val store: SignUpStore = instanceKeeper.getStore {
		signUpStoreFactory.create()
	}

	private val errorEvents = MutableSharedFlow<SignUpError>(extraBufferCapacity = 1)

	override val model: Value<SignUpStore.State> = store.asValue()

	override val errors: Flow<SignUpError> = errorEvents.asSharedFlow()

	init {
		launchTry {
			store.labels.collect { label ->
				when (label) {
					SignUpStore.Label.OpenSignIn -> signUpRouter.navigateToSignIn()
					SignUpStore.Label.OpenMainHost -> signUpRouter.navigateToMain()
					SignUpStore.Label.ShowRegistrationError -> errorEvents.emit(SignUpError.RegistrationError)
					SignUpStore.Label.ShowNoConnection -> errorEvents.emit(SignUpError.NoConnection)
					SignUpStore.Label.ShowUnknownError -> errorEvents.emit(SignUpError.Unknown)
				}
			}
		} catch {
			// ignored
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
}
