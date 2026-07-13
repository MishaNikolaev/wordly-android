package com.nmichail.wordly.android.core.validation.password

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor(
	passwordValidation: PasswordValidation,
) : (String) -> PasswordValidationItem by passwordValidation::invoke
