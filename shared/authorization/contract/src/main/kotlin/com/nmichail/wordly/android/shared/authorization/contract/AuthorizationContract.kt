package com.nmichail.wordly.android.shared.authorization.contract

import com.nmichail.wordly.android.component.contract.NavigationConfig
import com.nmichail.wordly.android.component.contract.NavigationResultContract

sealed interface AuthorizationConfig : NavigationConfig {

	data object SignIn : AuthorizationConfig

	data object SignUp : AuthorizationConfig
}

object SignUpResultContract : NavigationResultContract<String> {

	override val key: String = "SIGN_UP_RESULT"
}