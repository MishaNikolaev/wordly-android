package com.nmichail.wordly.android.core.validation.name

import com.nmichail.wordly.android.core.validation.InvalidState

enum class NameInvalidState : InvalidState {
	EMPTY_NAME,
	INVALID_MAX_LENGTH,
	INVALID_MIN_LENGTH,
	INVALID_NAME,
}
