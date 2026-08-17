package com.nmichail.wordly.android.features.authorization.signin.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
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
        Store<SignInStore.Intent, SignInStore.State, SignInStore.Label> by storeFactory.create(
            name = "SignInStore",
            initialState = SignInStore.State.Initial,
            bootstrapper = SimpleBootstrapper(Action.Init),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl,
        ) {}

    private sealed interface Action {

        data object Init : Action
    }

    private sealed interface Msg {

        data object Initialized : Msg

        data class ChangeEmail(val email: EmailValidationItem) : Msg

        data class ChangePassword(val password: PasswordValidationItem) : Msg

        data class SetSubmitting(val submitting: Boolean) : Msg

        data class SetError(val content: SignInStore.State.Content) : Msg

        data object RestoreContent : Msg
    }

    private inner class ExecutorImpl :
        BaseCoroutineExecutor<SignInStore.Intent, Action, SignInStore.State, Msg, SignInStore.Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                Action.Init -> dispatch(Msg.Initialized)
            }
        }

        override fun executeIntent(intent: SignInStore.Intent) {
            when (intent) {
                is SignInStore.Intent.ChangeEmail -> {
                    dispatch(Msg.ChangeEmail(validateEmailUseCase(intent.email.trim())))
                }

                is SignInStore.Intent.ChangePassword -> {
                    dispatch(Msg.ChangePassword(validatePasswordUseCase(intent.password)))
                }

                SignInStore.Intent.NavigateToSignUp -> {
                    val content = state() as? SignInStore.State.Content ?: return
                    if (!content.submitting) {
                        publish(SignInStore.Label.OpenSignUp)
                    }
                }

                SignInStore.Intent.Submit -> submit()
                SignInStore.Intent.Retry -> dispatch(Msg.RestoreContent)
            }
        }

        private fun submit() {
            val currentState = state() as? SignInStore.State.Content ?: return
            if (currentState.submitting) return

            val email = validateEmailUseCase(currentState.email.data.trim())
            val password = validatePasswordUseCase(currentState.password.data)
            dispatch(Msg.ChangeEmail(email))
            dispatch(Msg.ChangePassword(password))
            if (!email.isValid() || !password.isValid()) return

            dispatch(Msg.SetSubmitting(submitting = true))
            scope.launch {
                try {
                    val tokens = signInUseCase(
                        SignInData(
                            email = email.data,
                            password = password.data,
                        ),
                    )
                    saveAuthTokensUseCase(tokens)
                    dispatch(Msg.SetSubmitting(submitting = false))
                    publish(SignInStore.Label.OpenMainHost)
                } catch (error: Exception) {
                    handleError(error)
                }
            }
        }

        private fun handleError(error: Exception) {
            val networkError = networkExceptionConverter.convert(error)
            if (errorDelegate.handleError(networkError) == HandleErrorResult.HANDLED) {
                dispatch(Msg.SetSubmitting(submitting = false))
                return
            }
            val content =
                (state() as? SignInStore.State.Content)?.copy(submitting = false) ?: return
            dispatch(Msg.SetError(content = content))
        }
    }

    private object ReducerImpl : Reducer<SignInStore.State, Msg> {

        override fun SignInStore.State.reduce(msg: Msg): SignInStore.State {
            val content = this as? SignInStore.State.Content
            return when (msg) {
                Msg.Initialized -> SignInStore.State.Content(
                    email = EmailValidationItem(),
                    password = PasswordValidationItem(),
                    submitting = false,
                )

                is Msg.ChangeEmail -> content?.copy(email = msg.email) ?: this
                is Msg.ChangePassword -> content?.copy(password = msg.password) ?: this
                is Msg.SetSubmitting -> content?.copy(submitting = msg.submitting) ?: this
                is Msg.SetError -> SignInStore.State.Error(content = msg.content)
                Msg.RestoreContent -> (this as? SignInStore.State.Error)?.content ?: this
            }
        }
    }
}