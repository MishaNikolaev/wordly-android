package com.nmichail.wordly.android.core.validation.password

import com.nmichail.wordly.android.core.validation.InvalidState

sealed interface PasswordInvalidState : InvalidState {

	data object EMPTY_PASSWORD : PasswordInvalidState

	data object INVALID_MIN_LENGTH : PasswordInvalidState

	data object INVALID_MAX_LENGTH : PasswordInvalidState
}
