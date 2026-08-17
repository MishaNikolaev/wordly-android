package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem

interface SignUpStore :
    Store<SignUpStore.Intent, SignUpStore.State, SignUpStore.Label> {

    sealed interface State {

        data object Initial : State

        data object Loading : State

        data class Content(
            val email: EmailValidationItem,
            val password: PasswordValidationItem,
            val firstName: NameValidationItem,
            val lastName: NameValidationItem,
            val englishLevel: NotEmptyValidationItem,
            val submitting: Boolean,
        ) : State

        data class Error(val content: Content) : State
    }

    sealed interface Label {

        data object OpenSignIn : Label

        data object OpenMainHost : Label
    }

    sealed interface Intent {

        data class ChangeEmail(val email: String) : Intent

        data class ChangePassword(val password: String) : Intent

        data class ChangeFirstName(val firstName: String) : Intent

        data class ChangeLastName(val lastName: String) : Intent

        data class ChangeEnglishLevel(val englishLevel: String) : Intent

        data object Submit : Intent

        data object NavigateToSignIn : Intent

        data object Retry : Intent
    }
}
