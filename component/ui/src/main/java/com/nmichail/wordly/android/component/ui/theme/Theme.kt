package com.nmichail.wordly.android.component.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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
	tertiary = WordlyColors.LightSuccess,
	onTertiary = Color.White,
	tertiaryContainer = WordlyColors.LightWarning,
	onTertiaryContainer = Color.White,
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
	tertiary = WordlyColors.DarkSuccess,
	onTertiary = WordlyColors.DarkOnSecondary,
	tertiaryContainer = WordlyColors.DarkWarning,
	onTertiaryContainer = WordlyColors.DarkOnSecondary,
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
	surfaceContainerHigh = WordlyColors.DarkSurfaceVariant,
)

@Composable
fun WordlyAndroidTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit,
) {
	val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		shapes = Shapes,
		content = content,
	)
}