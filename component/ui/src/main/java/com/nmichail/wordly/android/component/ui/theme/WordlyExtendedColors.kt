package com.nmichail.wordly.android.component.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Cross-cutting semantic colors beyond Material3 [androidx.compose.material3.ColorScheme].
 *
 * Use [MaterialTheme.colorScheme] for surfaces/primary/error.
 * Use [WordlyTheme.colors] for success/warning/streak and soft accents.
 * Feature-specific tags (category, repeat date, etc.) stay in features.
 */
@Immutable
data class WordlyExtendedColors(
	val success: Color,
	val onSuccess: Color,
	val successContainer: Color,
	val warning: Color,
	val onWarning: Color,
	val warningContainer: Color,
	val streak: Color,
	val streakContainer: Color,
	val errorContainer: Color,
	val muted: Color,
	val outlineSoft: Color,
	val softShadow: Color,
	val primaryMuted: Color,
)

internal val LightExtendedColors = WordlyExtendedColors(
	success = WordlyColors.LightSuccess,
	onSuccess = Color.White,
	successContainer = WordlyColors.LightSuccessContainer,
	warning = WordlyColors.LightWarning,
	onWarning = Color.White,
	warningContainer = WordlyColors.LightWarningContainer,
	streak = WordlyColors.LightStreak,
	streakContainer = WordlyColors.LightStreakContainer,
	errorContainer = WordlyColors.LightErrorContainer,
	muted = WordlyColors.LightOnSurfaceVariant2,
	outlineSoft = WordlyColors.LightOutline.copy(alpha = 0.55f),
	softShadow = Color.White.copy(alpha = 0.08f),
	primaryMuted = WordlyColors.Primary.copy(alpha = 0.16f),
)

internal val DarkExtendedColors = WordlyExtendedColors(
	success = WordlyColors.DarkSuccess,
	onSuccess = WordlyColors.DarkOnSecondary,
	successContainer = WordlyColors.DarkSuccessContainer,
	warning = WordlyColors.DarkWarning,
	onWarning = WordlyColors.DarkOnSecondary,
	warningContainer = WordlyColors.DarkWarningContainer,
	streak = WordlyColors.DarkStreak,
	streakContainer = WordlyColors.DarkStreakContainer,
	errorContainer = WordlyColors.DarkErrorContainer,
	muted = WordlyColors.DarkOnSurfaceVariant2,
	outlineSoft = WordlyColors.DarkOutline.copy(alpha = 0.7f),
	softShadow = Color.White.copy(alpha = 0.08f),
	primaryMuted = WordlyColors.DarkPrimary.copy(alpha = 0.16f),
)

val LocalWordlyExtendedColors = staticCompositionLocalOf { LightExtendedColors }

object WordlyTheme {
	val colors: WordlyExtendedColors
		@Composable
		@ReadOnlyComposable
		get() = LocalWordlyExtendedColors.current

	val isDark: Boolean
		@Composable
		@ReadOnlyComposable
		get() = LocalDarkTheme.current
}
