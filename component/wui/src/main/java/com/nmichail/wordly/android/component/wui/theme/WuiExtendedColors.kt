package com.nmichail.wordly.android.component.wui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Cross-cutting semantic colors beyond Material3 [androidx.compose.material3.ColorScheme].
 *
 * Use [MaterialTheme.colorScheme] for surfaces/primary/error.
 * Use [Wui.colors] for success/warning/streak/recapCta and soft accents.
 * Feature-specific tags (category, repeat date, etc.) stay in features.
 */
@Immutable
data class WuiExtendedColors(
	val success: Color,
	val onSuccess: Color,
	val successContainer: Color,
	val warning: Color,
	val onWarning: Color,
	val warningContainer: Color,
	val streak: Color,
	val streakContainer: Color,
	val recapCta: Color,
	val errorContainer: Color,
	val muted: Color,
	val outlineSoft: Color,
	val softShadow: Color,
	val primaryMuted: Color,
)

internal val LightExtendedColors = WuiExtendedColors(
	success = WuiColors.LightSuccess,
	onSuccess = Color.White,
	successContainer = WuiColors.LightSuccessContainer,
	warning = WuiColors.LightWarning,
	onWarning = Color.White,
	warningContainer = WuiColors.LightWarningContainer,
	streak = WuiColors.LightStreak,
	streakContainer = WuiColors.LightStreakContainer,
	recapCta = WuiColors.LightRecapCta,
	errorContainer = WuiColors.LightErrorContainer,
	muted = WuiColors.LightOnSurfaceVariant2,
	outlineSoft = WuiColors.LightOutline.copy(alpha = 0.55f),
	softShadow = Color.White.copy(alpha = 0.08f),
	primaryMuted = WuiColors.Primary.copy(alpha = 0.16f),
)

internal val DarkExtendedColors = WuiExtendedColors(
	success = WuiColors.DarkSuccess,
	onSuccess = WuiColors.DarkOnSecondary,
	successContainer = WuiColors.DarkSuccessContainer,
	warning = WuiColors.DarkWarning,
	onWarning = WuiColors.DarkOnSecondary,
	warningContainer = WuiColors.DarkWarningContainer,
	streak = WuiColors.DarkStreak,
	streakContainer = WuiColors.DarkStreakContainer,
	recapCta = WuiColors.DarkRecapCta,
	errorContainer = WuiColors.DarkErrorContainer,
	muted = WuiColors.DarkOnSurfaceVariant2,
	outlineSoft = WuiColors.DarkOutline.copy(alpha = 0.7f),
	softShadow = Color.White.copy(alpha = 0.08f),
	primaryMuted = WuiColors.DarkPrimary.copy(alpha = 0.16f),
)

val LocalWuiExtendedColors = staticCompositionLocalOf { LightExtendedColors }

object Wui {
	val colors: WuiExtendedColors
		@Composable
		@ReadOnlyComposable
		get() = LocalWuiExtendedColors.current

	val isDark: Boolean
		@Composable
		@ReadOnlyComposable
		get() = LocalDarkTheme.current
}
