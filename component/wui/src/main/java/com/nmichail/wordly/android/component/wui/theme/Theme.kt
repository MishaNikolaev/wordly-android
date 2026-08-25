package com.nmichail.wordly.android.component.wui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
	primary = WuiColors.Primary,
	onPrimary = WuiColors.OnPrimary,
	primaryContainer = WuiColors.LightPrimaryContainer,
	onPrimaryContainer = WuiColors.LightOnPrimaryContainer,
	inversePrimary = WuiColors.LightOnPrimaryContainer,
	secondary = WuiColors.LightSecondary,
	onSecondary = WuiColors.LightOnSecondary,
	secondaryContainer = WuiColors.LightSecondaryContainer,
	onSecondaryContainer = WuiColors.LightSecondary,
	tertiary = WuiColors.LightSecondary,
	onTertiary = WuiColors.LightOnSecondary,
	tertiaryContainer = WuiColors.LightSecondaryContainer,
	onTertiaryContainer = WuiColors.LightSecondary,
	background = WuiColors.LightBackground,
	onBackground = WuiColors.LightOnSurface,
	surface = WuiColors.LightSurface,
	onSurface = WuiColors.LightOnSurface,
	surfaceVariant = WuiColors.LightSurfaceVariant,
	onSurfaceVariant = WuiColors.LightOnSurfaceVariant,
	surfaceBright = WuiColors.LightSurface,
	outline = WuiColors.LightOutline,
	error = WuiColors.LightError,
	onError = Color.White,
	errorContainer = WuiColors.LightErrorContainer,
	onErrorContainer = WuiColors.LightError,
	surfaceContainerHigh = WuiColors.LightSurface,
)

private val DarkColorScheme = darkColorScheme(
	primary = WuiColors.DarkPrimary,
	onPrimary = WuiColors.DarkOnPrimary,
	primaryContainer = WuiColors.DarkPrimaryContainer,
	onPrimaryContainer = WuiColors.DarkOnPrimaryContainer,
	inversePrimary = WuiColors.DarkPrimary,
	secondary = WuiColors.DarkSecondary,
	onSecondary = WuiColors.DarkOnSecondary,
	secondaryContainer = WuiColors.DarkSecondaryContainer,
	onSecondaryContainer = WuiColors.DarkSecondary,
	tertiary = WuiColors.DarkSecondary,
	onTertiary = WuiColors.DarkOnSecondary,
	tertiaryContainer = WuiColors.DarkSecondaryContainer,
	onTertiaryContainer = WuiColors.DarkSecondary,
	background = WuiColors.DarkBackground,
	onBackground = WuiColors.DarkOnSurface,
	surface = WuiColors.DarkSurface,
	onSurface = WuiColors.DarkOnSurface,
	surfaceVariant = WuiColors.DarkSurfaceVariant,
	onSurfaceVariant = WuiColors.DarkOnSurfaceVariant,
	surfaceBright = WuiColors.DarkSurface,
	outline = WuiColors.DarkOutline,
	error = WuiColors.DarkError,
	onError = WuiColors.DarkOnSecondary,
	errorContainer = WuiColors.DarkErrorContainer,
	onErrorContainer = WuiColors.DarkError,
	surfaceContainerHigh = WuiColors.DarkSurfaceVariant,
)

val LocalDarkTheme = staticCompositionLocalOf { false }

@Composable
fun WuiTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit,
) {
	val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
	val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			(view.context as? Activity)?.window?.apply {
				statusBarColor = colorScheme.background.toArgb()
				navigationBarColor = colorScheme.background.toArgb()
				WindowCompat.getInsetsController(this, view).apply {
					isAppearanceLightStatusBars = !darkTheme
					isAppearanceLightNavigationBars = !darkTheme
				}
			}
		}
	}

	CompositionLocalProvider(
		LocalDarkTheme provides darkTheme,
		LocalWuiExtendedColors provides extendedColors,
	) {
		MaterialTheme(
			colorScheme = colorScheme,
			typography = Typography,
			shapes = Shapes,
			content = content,
		)
	}
}

@Composable
@ReadOnlyComposable
fun isAppInDarkTheme(): Boolean = LocalDarkTheme.current
