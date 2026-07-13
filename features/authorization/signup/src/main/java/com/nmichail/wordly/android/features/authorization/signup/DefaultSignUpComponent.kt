package com.nmichail.wordly.android.features.authorization.signup

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.authorization.signup.presentation.DefaultSignUpStore
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpStore
import com.nmichail.wordly.android.shared.authorization.contract.SignUpComponent

class DefaultSignUpComponent(
	componentContext: ComponentContext,
	val store: SignUpStore = DefaultSignUpStore(),
) : ComponentContext by componentContext, SignUpComponent
