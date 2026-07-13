package com.nmichail.wordly.android.core.validation.name

import javax.inject.Inject

class ValidateNameUseCase @Inject constructor(
	nameValidation: NameValidation,
) : (String, NamePart) -> NameValidationItem by nameValidation::invoke
