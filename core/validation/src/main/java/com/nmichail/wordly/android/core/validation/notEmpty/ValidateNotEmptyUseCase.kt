package com.nmichail.wordly.android.core.validation.notEmpty

class ValidateNotEmptyUseCase {

	operator fun invoke(data: String): NotEmptyValidationItem =
		if (data.isNotBlank()) {
			NotEmptyValidationItem(data = data, validationState = NotEmptyValidationState.Valid)
		} else {
			NotEmptyValidationItem(data = data, validationState = NotEmptyValidationState.Invalid)
		}
}