package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.nmichail.wordly.android.core.validation.email.isValid
import com.nmichail.wordly.android.core.validation.name.isValid
import com.nmichail.wordly.android.core.validation.notEmpty.isValid
import com.nmichail.wordly.android.core.validation.password.isValid

internal fun SignUpStore.State.Content.areFieldsValid(): Boolean =
	email.isValid()
		&& password.isValid()
		&& firstName.isValid()
		&& lastName.isValid()
		&& englishLevel.isValid()
