package com.nmichail.wordly.android.mainhost.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import com.nmichail.wordly.android.features.authorization.signin.ui.SignInContent
import com.nmichail.wordly.android.features.authorization.signup.ui.SignUpContent
import com.nmichail.wordly.android.mainhost.presentation.RootComponent

@Composable
fun RootContent(
	component: RootComponent,
	modifier: Modifier = Modifier,
) {
	WordlyAndroidTheme {
		Box(modifier = modifier.fillMaxSize()) {
			Children(stack = component.stack) { child ->
				when (val instance = child.instance) {
					is RootComponent.Child.SignIn -> SignInContent(
						component = instance.component,
					)

					is RootComponent.Child.SignUp -> SignUpContent(
						component = instance.component,
					)
				}
			}
		}
	}
}