package com.nmichail.wordly.android.features.authorization.signin

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.authorization.signin.presentation.DefaultSignInStore
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInStore
import com.nmichail.wordly.android.shared.authorization.contract.SignInComponent

class DefaultSignInComponent(
	componentContext: ComponentContext,
	val store: SignInStore = DefaultSignInStore(),
) : ComponentContext by componentContext, SignInComponent
