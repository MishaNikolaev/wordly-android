package com.nmichail.wordly.android.mainhost.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.authorization.signin.ui.SignInContent
import com.nmichail.wordly.android.features.authorization.signup.ui.SignUpContent
import com.nmichail.wordly.android.features.dev.networkselection.ui.NetworkSelectionContent
import com.nmichail.wordly.android.mainhost.presentation.RootComponent

@Composable
fun RootContent(
	component: RootComponent,
	devEnabled: Boolean,
	themeMode: AppThemeMode,
	modifier: Modifier = Modifier,
) {
	val darkTheme = when (themeMode) {
		AppThemeMode.System -> isSystemInDarkTheme()
		AppThemeMode.Light -> false
		AppThemeMode.Dark -> true
	}

	WuiTheme(darkTheme = darkTheme) {
		Box(modifier = modifier.fillMaxSize()) {
			Children(stack = component.stack) { child ->
				when (val instance = child.instance) {
					is RootComponent.Child.SignIn -> SignInContent(
						component = instance.component,
						devEnabled = devEnabled,
					)

					is RootComponent.Child.SignUp -> SignUpContent(
						component = instance.component,
					)

					is RootComponent.Child.MainHost -> MainHostContent(
						component = instance.component,
						themeMode = themeMode,
					)

					is RootComponent.Child.NetworkSelection -> NetworkSelectionContent(
						component = instance.component,
					)
				}
			}
		}
	}
}
