package com.nmichail.wordly.android.component.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
	primary = WordlyColors.Primary,
	onPrimary = WordlyColors.OnPrimary,
	primaryContainer = WordlyColors.LightPrimaryContainer,
	onPrimaryContainer = WordlyColors.LightOnPrimaryContainer,
	inversePrimary = WordlyColors.LightOnPrimaryContainer,
	secondary = WordlyColors.LightSecondary,
	onSecondary = WordlyColors.LightOnSecondary,
	secondaryContainer = WordlyColors.LightSecondaryContainer,
	onSecondaryContainer = WordlyColors.LightSecondary,
	// Success lives only on WordlyTheme.colors — do not alias M3 tertiary to it.
	tertiary = WordlyColors.LightSecondary,
	onTertiary = WordlyColors.LightOnSecondary,
	tertiaryContainer = WordlyColors.LightSecondaryContainer,
	onTertiaryContainer = WordlyColors.LightSecondary,
	background = WordlyColors.LightBackground,
	onBackground = WordlyColors.LightOnSurface,
	surface = WordlyColors.LightSurface,
	onSurface = WordlyColors.LightOnSurface,
	surfaceVariant = WordlyColors.LightSurfaceVariant,
	onSurfaceVariant = WordlyColors.LightOnSurfaceVariant,
	surfaceBright = WordlyColors.LightSurface,
	outline = WordlyColors.LightOutline,
	error = WordlyColors.LightError,
	onError = Color.White,
	errorContainer = WordlyColors.LightErrorContainer,
	onErrorContainer = WordlyColors.LightError,
	surfaceContainerHigh = WordlyColors.LightSurface,
)

private val DarkColorScheme = darkColorScheme(
	primary = WordlyColors.DarkPrimary,
	onPrimary = WordlyColors.DarkOnPrimary,
	primaryContainer = WordlyColors.DarkPrimaryContainer,
	onPrimaryContainer = WordlyColors.DarkOnPrimaryContainer,
	inversePrimary = WordlyColors.DarkPrimary,
	secondary = WordlyColors.DarkSecondary,
	onSecondary = WordlyColors.DarkOnSecondary,
	secondaryContainer = WordlyColors.DarkSecondaryContainer,
	onSecondaryContainer = WordlyColors.DarkSecondary,
	tertiary = WordlyColors.DarkSecondary,
	onTertiary = WordlyColors.DarkOnSecondary,
	tertiaryContainer = WordlyColors.DarkSecondaryContainer,
	onTertiaryContainer = WordlyColors.DarkSecondary,
	background = WordlyColors.DarkBackground,
	onBackground = WordlyColors.DarkOnSurface,
	surface = WordlyColors.DarkSurface,
	onSurface = WordlyColors.DarkOnSurface,
	surfaceVariant = WordlyColors.DarkSurfaceVariant,
	onSurfaceVariant = WordlyColors.DarkOnSurfaceVariant,
	surfaceBright = WordlyColors.DarkSurface,
	outline = WordlyColors.DarkOutline,
	error = WordlyColors.DarkError,
	onError = WordlyColors.DarkOnSecondary,
	errorContainer = WordlyColors.DarkErrorContainer,
	onErrorContainer = WordlyColors.DarkError,
	surfaceContainerHigh = WordlyColors.DarkSurfaceVariant,
)

val LocalDarkTheme = staticCompositionLocalOf { false }

@Composable
fun WordlyAndroidTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit,
) {
	val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
	val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

	CompositionLocalProvider(
		LocalDarkTheme provides darkTheme,
		LocalWordlyExtendedColors provides extendedColors,
	) {
		MaterialTheme(
			colorScheme = colorScheme,
			typography = Typography,
			shapes = Shapes,
			content = content,
		)
	}
}

/** Reflects in-app theme from [WordlyAndroidTheme], not inferred luminance. */
@Composable
@ReadOnlyComposable
fun isAppInDarkTheme(): Boolean = LocalDarkTheme.current
