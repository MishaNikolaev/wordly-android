package com.nmichail.wordly.android.features.mainhost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.mainhost.authorization.AuthorizationComponent

@Composable
fun MainHostContent(
	component: RootComponent,
	modifier: Modifier = Modifier,
) {
	val childStack = component.childStack.subscribeAsState()

	Children(
		stack = childStack.value,
		modifier = modifier,
	) { child ->
		when (val instance = child.instance) {
			is RootComponent.Child.Authorization -> AuthorizationContent(instance.component)
		}
	}
}

@Composable
private fun AuthorizationContent(
	component: AuthorizationComponent,
	modifier: Modifier = Modifier,
) {
	val childStack = component.childStack.subscribeAsState()

	Children(
		stack = childStack.value,
		modifier = modifier,
	) { child ->
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center,
		) {
			val title = when (child.instance) {
				AuthorizationComponent.Child.SignIn -> "Sign In"
				AuthorizationComponent.Child.SignUp -> "Sign Up"
			}
			Text(
				text = title,
				style = MaterialTheme.typography.headlineMedium,
			)
		}
	}
}