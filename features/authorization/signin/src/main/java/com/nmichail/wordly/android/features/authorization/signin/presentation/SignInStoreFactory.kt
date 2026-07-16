package com.nmichail.wordly.android.features.authorization.signin.presentation

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
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase
import com.nmichail.wordly.android.core.validation.password.isValid
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import com.nmichail.wordly.android.features.authorization.signin.domain.usecase.SignInUseCase
import com.nmichail.wordly.android.shared.error.NetworkExceptionConverter
import com.nmichail.wordly.android.shared.error.StatusCodes
import com.nmichail.wordly.android.shared.error.messageIdOrNull
import com.nmichail.wordly.android.shared.error.presentation.ErrorDelegate
import com.nmichail.wordly.android.shared.error.presentation.HandleErrorResult
import javax.inject.Inject

internal class SignInStoreFactory @Inject constructor(
	private val validateEmailUseCase: ValidateEmailUseCase,
	private val validatePasswordUseCase: ValidatePasswordUseCase,
	private val signInUseCase: SignInUseCase,
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase,
	private val networkExceptionConverter: NetworkExceptionConverter,
	private val errorDelegate: ErrorDelegate,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): SignInStore = object : SignInStore,
		Store<SignInStore.Intent, SignInComponent.State, SignInComponent.Label> by storeFactory.create(
			name = "SignInStore",
			initialState = SignInComponent.State(),
			executorFactory = ::ExecutorImpl,
			reducer = ReducerImpl,
		) {}

	private sealed interface Msg {

		data class ChangeEmail(val email: EmailValidationItem) : Msg

		data class ChangePassword(val password: PasswordValidationItem) : Msg
	}

	private object ReducerImpl : Reducer<SignInComponent.State, Msg> {

		override fun SignInComponent.State.reduce(msg: Msg): SignInComponent.State =
			when (msg) {
				is Msg.ChangeEmail -> copy(email = msg.email)
				is Msg.ChangePassword -> copy(password = msg.password)
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<SignInStore.Intent, Nothing, SignInComponent.State, Msg, SignInComponent.Label>() {

		override fun executeIntent(intent: SignInStore.Intent) {
			when (intent) {
				is SignInStore.Intent.ChangeEmail -> dispatch(
					Msg.ChangeEmail(validateEmailUseCase(intent.email.trim())),
				)

				is SignInStore.Intent.ChangePassword -> dispatch(
					Msg.ChangePassword(validatePasswordUseCase(intent.password)),
				)

				SignInStore.Intent.Submit -> handleSubmit()

				SignInStore.Intent.NavigateToSignUp -> publish(SignInComponent.Label.OpenSignUp)
			}
		}

		private fun handleSubmit() {
			val currentState = state()
			val email = validateEmailUseCase(currentState.email.data.trim())
			val password = validatePasswordUseCase(currentState.password.data)
			dispatch(Msg.ChangeEmail(email))
			dispatch(Msg.ChangePassword(password))

			if (!email.isValid() || !password.isValid()) return

			launchTry {
				val tokens = signInUseCase(
					SignInData(
						email = email.data,
						password = password.data,
					),
				)
				saveAuthTokensUseCase(tokens)
				publish(SignInComponent.Label.OpenMainHost)
			} catch(::handleSignInError)
		}

		private fun handleSignInError(error: Exception) {
			val networkError = networkExceptionConverter.convert(error)
			if (errorDelegate.handleError(networkError) == HandleErrorResult.HANDLED) return

			when (networkError.messageIdOrNull()) {
				StatusCodes.NEEDS_AUTHORIZATION.statusCode,
				StatusCodes.AUTHORIZATION_FAILED.statusCode,
				-> publish(SignInComponent.Label.ShowInvalidCredentials)

				StatusCodes.NO_CONNECTION.statusCode -> publish(SignInComponent.Label.ShowNoConnection)

				else -> publish(SignInComponent.Label.ShowUnknownError)
			}
		}
	}
}
