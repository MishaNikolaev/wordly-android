package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.email.ValidateEmailUseCase
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.name.ValidateNameUseCase
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.ValidateNotEmptyUseCase
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase

class SignUpStoreFactory(
	private val validateEmailUseCase: ValidateEmailUseCase = ValidateEmailUseCase(),
	private val validatePasswordUseCase: ValidatePasswordUseCase = ValidatePasswordUseCase(),
	private val validateNameUseCase: ValidateNameUseCase = ValidateNameUseCase(),
	private val validateNotEmptyUseCase: ValidateNotEmptyUseCase = ValidateNotEmptyUseCase(),
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): SignUpStore = object : SignUpStore,
		Store<SignUpStore.Intent, SignUpStore.State, SignUpStore.Label> by storeFactory.create(
			name = "SignUpStore",
			initialState = SignUpStore.State(),
			executorFactory = ::ExecutorImpl,
			reducer = ReducerImpl,
		) {}

	private sealed interface Msg {

		data class ChangeEmail(val email: EmailValidationItem) : Msg

		data class ChangePassword(val password: PasswordValidationItem) : Msg

		data class ChangeFirstName(val firstName: NameValidationItem) : Msg

		data class ChangeLastName(val lastName: NameValidationItem) : Msg

		data class ChangeEnglishLevel(val englishLevel: NotEmptyValidationItem) : Msg
	}

	private object ReducerImpl : Reducer<SignUpStore.State, Msg> {

		override fun SignUpStore.State.reduce(msg: Msg): SignUpStore.State =
			when (msg) {
				is Msg.ChangeEmail -> copy(email = msg.email)
				is Msg.ChangePassword -> copy(password = msg.password)
				is Msg.ChangeFirstName -> copy(firstName = msg.firstName)
				is Msg.ChangeLastName -> copy(lastName = msg.lastName)
				is Msg.ChangeEnglishLevel -> copy(englishLevel = msg.englishLevel)
			}
	}

	private inner class ExecutorImpl :
		CoroutineExecutor<SignUpStore.Intent, Nothing, SignUpStore.State, Msg, SignUpStore.Label>() {

		override fun executeIntent(intent: SignUpStore.Intent) {
			when (intent) {
				is SignUpStore.Intent.ChangeEmail -> dispatch(
					Msg.ChangeEmail(validateEmailUseCase(intent.email.trim())),
				)

				is SignUpStore.Intent.ChangePassword -> dispatch(
					Msg.ChangePassword(validatePasswordUseCase(intent.password)),
				)

				is SignUpStore.Intent.ChangeFirstName -> dispatch(
					Msg.ChangeFirstName(validateNameUseCase(intent.firstName.trim(), NamePart.NAME)),
				)

				is SignUpStore.Intent.ChangeLastName -> dispatch(
					Msg.ChangeLastName(validateNameUseCase(intent.lastName.trim(), NamePart.SURNAME)),
				)

				is SignUpStore.Intent.ChangeEnglishLevel -> dispatch(
					Msg.ChangeEnglishLevel(validateNotEmptyUseCase(intent.englishLevel)),
				)

				SignUpStore.Intent.Submit -> handleSubmit()

				SignUpStore.Intent.NavigateToSignIn -> publish(SignUpStore.Label.OpenSignIn)
			}
		}

		private fun handleSubmit() {
			val currentState = state()
			val email = validateEmailUseCase(currentState.email.data.trim())
			val password = validatePasswordUseCase(currentState.password.data)
			val firstName = validateNameUseCase(currentState.firstName.data.trim(), NamePart.NAME)
			val lastName = validateNameUseCase(currentState.lastName.data.trim(), NamePart.SURNAME)
			val englishLevel = validateNotEmptyUseCase(currentState.englishLevel.data)

			dispatch(Msg.ChangeEmail(email))
			dispatch(Msg.ChangePassword(password))
			dispatch(Msg.ChangeFirstName(firstName))
			dispatch(Msg.ChangeLastName(lastName))
			dispatch(Msg.ChangeEnglishLevel(englishLevel))

			if (
				SignUpStore.State(
					email = email,
					password = password,
					firstName = firstName,
					lastName = lastName,
					englishLevel = englishLevel,
				).areFieldsValid()
			) {
				Unit
			}
		}
	}
}