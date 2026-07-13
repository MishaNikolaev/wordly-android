package com.nmichail.wordly.android.core.validation.password

import com.nmichail.wordly.android.core.validation.InvalidState

enum class PasswordInvalidState : InvalidState {
	EMPTY_PASSWORD,
	INVALID_MIN_LENGTH,
	INVALID_MAX_LENGTH,
}
