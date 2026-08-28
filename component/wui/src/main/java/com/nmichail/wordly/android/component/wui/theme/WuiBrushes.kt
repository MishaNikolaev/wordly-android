package com.nmichail.wordly.android.component.wui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object WuiBrushes {

	private val DailyReviewGradientEnd = Offset(x = 900f, y = 700f)
	private const val HeroBackdropMidStop = 0.28f
	private const val HeroBackdropFadeStop = 0.52f

	val HeroBackdropHeight = 360.dp

	val MaterialHero = Brush.linearGradient(
		colors = listOf(
			WuiColors.Primary,
			WuiColors.HeroGradientMiddle,
			WuiColors.HeroGradientEnd,
		),
		start = Offset.Zero,
		end = Offset.Infinite,
	)

	val DailyReviewLight = Brush.linearGradient(
		colors = listOf(
			WuiColors.LightDailyReviewGradientStart,
			WuiColors.LightDailyReviewGradientEnd,
		),
		start = Offset.Zero,
		end = DailyReviewGradientEnd,
	)

	val DailyReviewDark = Brush.linearGradient(
		colors = listOf(
			WuiColors.DarkPrimaryContainer,
			WuiColors.DarkSurfaceVariant,
		),
		start = Offset.Zero,
		end = DailyReviewGradientEnd,
	)

	fun heroBackdropFadeFromTop(
		vivid: Color,
		mid: Color,
		background: Color,
	): Brush = Brush.verticalGradient(
		colorStops = arrayOf(
			0f to vivid,
			HeroBackdropMidStop to mid,
			HeroBackdropFadeStop to background,
			1f to background,
		),
	)
}