package com.nmichail.wordly.android.features.authorization.signin.ui

import com.nmichail.wordly.android.core.validation.email.isValid
import com.nmichail.wordly.android.core.validation.password.isValid
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInStore

fun SignInStore.State.areFieldsValid(): Boolean =
	email.isValid() && password.isValid()
