package com.nmichail.wordly.android.core.validation.notEmpty

import javax.inject.Inject

class ValidateNotEmptyUseCase @Inject constructor(
	notEmptyValidation: NotEmptyValidation,
) : (String) -> NotEmptyValidationItem by notEmptyValidation::invoke
