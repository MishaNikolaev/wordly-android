package com.nmichail.wordly.android.core.validation.password

import com.nmichail.wordly.android.core.validation.DefaultValidationState

class ValidatePasswordUseCase {

	operator fun invoke(password: String): PasswordValidationItem =
		when {
			PasswordValidator.isPasswordEmpty(password) -> PasswordValidationItem(
				data = password,
				validationState = DefaultValidationState.Invalid(PasswordInvalidState.EMPTY_PASSWORD),
			)

			PasswordValidator.isPasswordMinLengthInvalid(password) -> PasswordValidationItem(
				data = password,
				validationState = DefaultValidationState.Invalid(PasswordInvalidState.INVALID_MIN_LENGTH),
			)

			PasswordValidator.isPasswordMaxLengthInvalid(password) -> PasswordValidationItem(
				data = password,
				validationState = DefaultValidationState.Invalid(PasswordInvalidState.INVALID_MAX_LENGTH),
			)

			else -> PasswordValidationItem(
				data = password,
				validationState = DefaultValidationState.Valid,
			)
		}
}
