package com.nmichail.wordly.android.core.validation.name

import com.nmichail.wordly.android.core.validation.InvalidState

sealed interface NameInvalidState : InvalidState {

	data object EMPTY_NAME : NameInvalidState

	data object INVALID_MAX_LENGTH : NameInvalidState

	data object INVALID_MIN_LENGTH : NameInvalidState

	data object INVALID_NAME : NameInvalidState
}