package com.nmichail.wordly.android.core.validation.name

import com.nmichail.wordly.android.core.validation.DefaultValidationState

class ValidateNameUseCase {

	operator fun invoke(name: String, namePart: NamePart): NameValidationItem =
		NameValidationItem(
			data = name,
			validationState = getNameValidationState(name, namePart),
			namePart = namePart,
		)

	private fun getNameValidationState(name: String, namePart: NamePart): DefaultValidationState =
		when {
			NameValidator.isNameEmpty(name) -> DefaultValidationState.Invalid(NameInvalidState.EMPTY_NAME)
			NameValidator.isNameInvalid(name) -> DefaultValidationState.Invalid(NameInvalidState.INVALID_NAME)
			NameValidator.isNameMinLengthInvalid(name) -> DefaultValidationState.Invalid(NameInvalidState.INVALID_MIN_LENGTH)
			NameValidator.isNameMaxLengthInvalid(name) -> DefaultValidationState.Invalid(NameInvalidState.INVALID_MAX_LENGTH)
			else -> DefaultValidationState.Valid
		}
}