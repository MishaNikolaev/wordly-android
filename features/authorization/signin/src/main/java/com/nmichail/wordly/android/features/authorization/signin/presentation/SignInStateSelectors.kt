package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.nmichail.wordly.android.core.validation.email.isValid
import com.nmichail.wordly.android.core.validation.password.isValid

internal fun SignInComponent.State.areFieldsValid(): Boolean =
	email.isValid() && password.isValid()
