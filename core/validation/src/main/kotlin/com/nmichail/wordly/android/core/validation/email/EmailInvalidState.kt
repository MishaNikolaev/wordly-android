package com.nmichail.wordly.android.core.validation.email

import com.nmichail.wordly.android.core.validation.InvalidState

enum class EmailInvalidState : InvalidState {
	EMPTY_EMAIL,
	INVALID_LENGTH_BEFORE_AT_SIGN,
	INVALID_LENGTH,
	INVALID_EMAIL,
}
