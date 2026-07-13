package com.nmichail.wordly.android.features.authorization.signin.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import com.nmichail.wordly.android.features.authorization.signup.ui.SignUpContent
import com.nmichail.wordly.android.shared.authorization.contract.AuthorizationComponent

@Composable
fun AuthorizationContent(
	component: AuthorizationComponent,
	modifier: Modifier = Modifier,
) {
	val childStack = component.childStack.subscribeAsState()

	Children(
		stack = childStack.value,
		modifier = modifier,
	) { child ->
		when (val instance = child.instance) {
			is AuthorizationComponent.Child.SignIn -> SignInContent(
				component = instance.component as SignInComponent,
			)
			is AuthorizationComponent.Child.SignUp -> SignUpContent(
				component = instance.component as SignUpComponent,
			)
		}
	}
}
