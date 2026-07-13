package com.nmichail.wordly.android.core.validation.email

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import javax.inject.Inject

class EmailValidation @Inject constructor() {

	operator fun invoke(email: String): EmailValidationItem =
		when {
			!EmailValidator.isEmailLengthBeforeAtValid(email) -> EmailValidationItem(
				data = email,
				validationState = DefaultValidationState.Invalid(EmailInvalidState.INVALID_LENGTH_BEFORE_AT_SIGN),
			)

			EmailValidator.isEmailEmpty(email) -> EmailValidationItem(
				data = email,
				validationState = DefaultValidationState.Invalid(EmailInvalidState.EMPTY_EMAIL),
			)

			!EmailValidator.isEmailLengthAllValid(email) -> EmailValidationItem(
				data = email,
				validationState = DefaultValidationState.Invalid(EmailInvalidState.INVALID_LENGTH),
			)

			EmailValidator.isEmailInvalid(email) -> EmailValidationItem(
				data = email,
				validationState = DefaultValidationState.Invalid(EmailInvalidState.INVALID_EMAIL),
			)

			else -> EmailValidationItem(
				data = email,
				validationState = DefaultValidationState.Valid,
			)
		}
}
