package com.nmichail.wordly.android.features.mainhost.authorization

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.shared.authorization.contract.AuthorizationConfig

interface AuthorizationComponent {

	val childStack: Value<ChildStack<AuthorizationConfig, Child>>

	sealed interface Child {

		data object SignIn : Child

		data object SignUp : Child
	}
}
