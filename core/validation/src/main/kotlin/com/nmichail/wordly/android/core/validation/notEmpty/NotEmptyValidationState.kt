package com.nmichail.wordly.android.core.validation.notEmpty

import com.nmichail.wordly.android.core.validation.ValidationState

sealed class NotEmptyValidationState : ValidationState {

	data object Unverified : NotEmptyValidationState()

	data object Valid : NotEmptyValidationState()

	data object Invalid : NotEmptyValidationState()
}
