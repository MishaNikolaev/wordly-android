package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.email.ValidateEmailUseCase
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase

class SignInStoreFactory(
	private val validateEmailUseCase: ValidateEmailUseCase = ValidateEmailUseCase(),
	private val validatePasswordUseCase: ValidatePasswordUseCase = ValidatePasswordUseCase(),
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): SignInStore = object : SignInStore,
		Store<SignInStore.Intent, SignInStore.State, SignInStore.Label> by storeFactory.create(
			name = "SignInStore",
			initialState = SignInStore.State(),
			executorFactory = ::ExecutorImpl,
			reducer = ReducerImpl,
		) {}

	private sealed interface Msg {

		data class ChangeEmail(val email: EmailValidationItem) : Msg

		data class ChangePassword(val password: PasswordValidationItem) : Msg
	}

	private object ReducerImpl : Reducer<SignInStore.State, Msg> {

		override fun SignInStore.State.reduce(msg: Msg): SignInStore.State =
			when (msg) {
				is Msg.ChangeEmail -> copy(email = msg.email)
				is Msg.ChangePassword -> copy(password = msg.password)
			}
	}

	private inner class ExecutorImpl :
		CoroutineExecutor<SignInStore.Intent, Nothing, SignInStore.State, Msg, SignInStore.Label>() {

		override fun executeIntent(intent: SignInStore.Intent) {
			when (intent) {
				is SignInStore.Intent.ChangeEmail -> dispatch(
					Msg.ChangeEmail(validateEmailUseCase(intent.email.trim())),
				)

				is SignInStore.Intent.ChangePassword -> dispatch(
					Msg.ChangePassword(validatePasswordUseCase(intent.password)),
				)

				SignInStore.Intent.Submit -> handleSubmit()

				SignInStore.Intent.NavigateToSignUp -> publish(SignInStore.Label.OpenSignUp)
			}
		}

		private fun handleSubmit() {
			val currentState = state()
			val email = validateEmailUseCase(currentState.email.data.trim())
			val password = validatePasswordUseCase(currentState.password.data)
			dispatch(Msg.ChangeEmail(email))
			dispatch(Msg.ChangePassword(password))

			// TODO: Убрать потом это — переходить на главный экран только при валидных полях
			publish(SignInStore.Label.OpenMainHost)
		}
	}
}