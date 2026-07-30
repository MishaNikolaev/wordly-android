package com.nmichail.wordly.android.component.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

object WordlyBrushes {

	val MaterialHero = Brush.linearGradient(
		colors = listOf(
			WordlyColors.Primary,
			WordlyColors.HeroGradientMiddle,
			WordlyColors.HeroGradientEnd,
		),
		start = Offset.Zero,
		end = Offset.Infinite,
	)
}