package com.nmichail.wordly.android.core.validation.email

import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
	emailValidation: EmailValidation,
) : (String) -> EmailValidationItem by emailValidation::invoke
