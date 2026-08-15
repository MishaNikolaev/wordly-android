package com.nmichail.wordly.android.features.authorization.signup.presentation

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
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.name.ValidateNameUseCase
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.ValidateNotEmptyUseCase
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm
import com.nmichail.wordly.android.features.authorization.signup.domain.usecase.SignUpUseCase
import com.nmichail.wordly.android.shared.error.NetworkExceptionConverter
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
        Store<SignUpStore.Intent, SignUpStore.State, SignUpStore.Label> by storeFactory.create(
            name = "SignUpStore",
            initialState = SignUpStore.State.Initial,
            bootstrapper = SimpleBootstrapper(Action.Initialize),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl,
        ) {}

    private sealed interface Action {

        data object Initialize : Action
    }

    private sealed interface Msg {

        data object Initialized : Msg

        data class ChangeEmail(val email: EmailValidationItem) : Msg

        data class ChangePassword(val password: PasswordValidationItem) : Msg

        data class ChangeFirstName(val firstName: NameValidationItem) : Msg

        data class ChangeLastName(val lastName: NameValidationItem) : Msg

        data class ChangeEnglishLevel(val englishLevel: NotEmptyValidationItem) : Msg

        data class SetSubmitting(val submitting: Boolean) : Msg

        data class SetError(val content: SignUpStore.State.Content) : Msg

        data object RestoreContent : Msg
    }

    private inner class ExecutorImpl :
        BaseCoroutineExecutor<SignUpStore.Intent, Action, SignUpStore.State, Msg, SignUpStore.Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                Action.Initialize -> dispatch(Msg.Initialized)
            }
        }

        override fun executeIntent(intent: SignUpStore.Intent) {
            when (intent) {
                is SignUpStore.Intent.ChangeEmail -> {
                    dispatch(Msg.ChangeEmail(validateEmailUseCase(intent.email.trim())))
                }

                is SignUpStore.Intent.ChangePassword -> {
                    dispatch(Msg.ChangePassword(validatePasswordUseCase(intent.password)))
                }

                is SignUpStore.Intent.ChangeFirstName -> {
                    dispatch(
                        Msg.ChangeFirstName(
                            validateNameUseCase(
                                intent.firstName.trim(),
                                NamePart.NAME
                            )
                        )
                    )
                }

                is SignUpStore.Intent.ChangeLastName -> {
                    dispatch(
                        Msg.ChangeLastName(
                            validateNameUseCase(
                                intent.lastName.trim(),
                                NamePart.SURNAME
                            )
                        )
                    )
                }

                is SignUpStore.Intent.ChangeEnglishLevel -> {
                    dispatch(Msg.ChangeEnglishLevel(validateNotEmptyUseCase(intent.englishLevel)))
                }

                SignUpStore.Intent.NavigateToSignIn -> {
                    val content = state() as? SignUpStore.State.Content ?: return
                    if (!content.submitting) {
                        publish(SignUpStore.Label.OpenSignIn)
                    }
                }

                SignUpStore.Intent.Submit -> submit()

                SignUpStore.Intent.Retry -> dispatch(Msg.RestoreContent)
            }
        }

        private fun submit() {
            val currentState = state() as? SignUpStore.State.Content ?: return
            if (currentState.submitting) return

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

            val content = state() as? SignUpStore.State.Content ?: return
            if (!content.areFieldsValid()) return

            dispatch(Msg.SetSubmitting(submitting = true))
            scope.launch {
            	try {
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
            		dispatch(Msg.SetSubmitting(submitting = false))
            		publish(SignUpStore.Label.OpenMainHost)
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
                (state() as? SignUpStore.State.Content)?.copy(submitting = false) ?: return
            dispatch(Msg.SetError(content = content))
        }
    }

    private object ReducerImpl : Reducer<SignUpStore.State, Msg> {

        @Suppress("CyclomaticComplexMethod")
        override fun SignUpStore.State.reduce(msg: Msg): SignUpStore.State {
            val content = this as? SignUpStore.State.Content

            return when (msg) {
                Msg.Initialized -> SignUpStore.State.Content(
                    email = EmailValidationItem(),
                    password = PasswordValidationItem(),
                    firstName = NameValidationItem(namePart = NamePart.NAME),
                    lastName = NameValidationItem(namePart = NamePart.SURNAME),
                    englishLevel = NotEmptyValidationItem(),
                    submitting = false,
                )
                is Msg.ChangeEmail -> content?.copy(email = msg.email) ?: this
                is Msg.ChangePassword -> content?.copy(password = msg.password) ?: this
                is Msg.ChangeFirstName -> content?.copy(firstName = msg.firstName) ?: this
                is Msg.ChangeLastName -> content?.copy(lastName = msg.lastName) ?: this
                is Msg.ChangeEnglishLevel -> content?.copy(englishLevel = msg.englishLevel) ?: this
                is Msg.SetSubmitting -> content?.copy(submitting = msg.submitting) ?: this
                is Msg.SetError -> SignUpStore.State.Error(content = msg.content)
                Msg.RestoreContent -> (this as? SignUpStore.State.Error)?.content ?: this
            }
        }
    }
}