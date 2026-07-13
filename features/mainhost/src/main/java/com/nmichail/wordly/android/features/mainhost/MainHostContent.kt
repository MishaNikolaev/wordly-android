package com.nmichail.wordly.android.features.mainhost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.authorization.signin.ui.AuthorizationContent

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
