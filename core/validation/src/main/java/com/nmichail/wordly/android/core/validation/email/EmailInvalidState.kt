package com.nmichail.wordly.android.core.validation.email

import com.nmichail.wordly.android.core.validation.InvalidState

sealed interface EmailInvalidState : InvalidState {

	data object EMPTY_EMAIL : EmailInvalidState

	data object INVALID_LENGTH_BEFORE_AT_SIGN : EmailInvalidState

	data object INVALID_LENGTH : EmailInvalidState

	data object INVALID_EMAIL : EmailInvalidState
}
