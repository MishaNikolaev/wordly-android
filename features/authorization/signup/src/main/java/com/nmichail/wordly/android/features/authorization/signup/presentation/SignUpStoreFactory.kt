package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.core.preferences.domain.usecase.SaveAuthTokensUseCase
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.email.ValidateEmailUseCase
import com.nmichail.wordly.android.core.validation.email.isValid
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.name.ValidateNameUseCase
import com.nmichail.wordly.android.core.validation.name.isValid
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.ValidateNotEmptyUseCase
import com.nmichail.wordly.android.core.validation.notEmpty.isValid
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase
import com.nmichail.wordly.android.core.validation.password.isValid
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm
import com.nmichail.wordly.android.features.authorization.signup.domain.usecase.SignUpUseCase
import com.nmichail.wordly.android.shared.error.NetworkExceptionConverter
import com.nmichail.wordly.android.shared.error.StatusCodes
import com.nmichail.wordly.android.shared.error.messageIdOrNull
import com.nmichail.wordly.android.shared.error.presentation.ErrorDelegate
import com.nmichail.wordly.android.shared.error.presentation.HandleErrorResult
import javax.inject.Inject

internal class SignUpStoreFactory @Inject constructor(
	private val validateEmailUseCase: ValidateEmailUseCase,
	private val validatePasswordUseCase: ValidatePasswordUseCase,
	private val validateNameUseCase: ValidateNameUseCase,
	private val validateNotEmptyUseCase: ValidateNotEmptyUseCase,
	private val signUpUseCase: SignUpUseCase,
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase,
	private val networkExceptionConverter: NetworkExceptionConverter,
	private val errorDelegate: ErrorDelegate,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): SignUpStore = object : SignUpStore,
		Store<SignUpStore.Intent, SignUpComponent.State, SignUpComponent.Label> by storeFactory.create(
			name = "SignUpStore",
			initialState = SignUpComponent.State(),
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

	private object ReducerImpl : Reducer<SignUpComponent.State, Msg> {

		override fun SignUpComponent.State.reduce(msg: Msg): SignUpComponent.State =
			when (msg) {
				is Msg.ChangeEmail -> copy(email = msg.email)
				is Msg.ChangePassword -> copy(password = msg.password)
				is Msg.ChangeFirstName -> copy(firstName = msg.firstName)
				is Msg.ChangeLastName -> copy(lastName = msg.lastName)
				is Msg.ChangeEnglishLevel -> copy(englishLevel = msg.englishLevel)
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<SignUpStore.Intent, Nothing, SignUpComponent.State, Msg, SignUpComponent.Label>() {

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

				SignUpStore.Intent.NavigateToSignIn -> publish(SignUpComponent.Label.OpenSignIn)
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

			if (!areFieldsValid(email, password, firstName, lastName, englishLevel)) return

			launchTry {
				val tokens = signUpUseCase(
					SignUpForm(
						email = email.data,
						password = password.data,
						firstName = firstName.data,
						lastName = lastName.data,
						englishLevel = englishLevel.data,
					),
				)
				saveAuthTokensUseCase(tokens)
				publish(SignUpComponent.Label.OpenMainHost)
			} catch(::handleSignUpError)
		}

		private fun handleSignUpError(error: Exception) {
			val networkError = networkExceptionConverter.convert(error)
			if (errorDelegate.handleError(networkError) == HandleErrorResult.HANDLED) return

			when (networkError.messageIdOrNull()) {
				StatusCodes.NO_CONNECTION.statusCode -> publish(SignUpComponent.Label.ShowNoConnection)
				else -> publish(SignUpComponent.Label.ShowRegistrationError)
			}
		}

		private fun areFieldsValid(
			email: EmailValidationItem,
			password: PasswordValidationItem,
			firstName: NameValidationItem,
			lastName: NameValidationItem,
			englishLevel: NotEmptyValidationItem,
		): Boolean =
			email.isValid() &&
				password.isValid() &&
				firstName.isValid() &&
				lastName.isValid() &&
				englishLevel.isValid()
	}
}