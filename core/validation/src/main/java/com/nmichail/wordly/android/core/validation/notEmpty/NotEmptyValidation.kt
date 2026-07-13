package com.nmichail.wordly.android.core.validation.notEmpty

import javax.inject.Inject

class NotEmptyValidation @Inject constructor() {

	operator fun invoke(data: String): NotEmptyValidationItem =
		if (data.isNotBlank()) {
			NotEmptyValidationItem(data = data, validationState = NotEmptyValidationState.Valid)
		} else {
			NotEmptyValidationItem(data = data, validationState = NotEmptyValidationState.Invalid)
		}
}
