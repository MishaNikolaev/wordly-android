package com.nmichail.wordly.android.component.wui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

object WuiBrushes {

	val MaterialHero = Brush.linearGradient(
		colors = listOf(
			WuiColors.Primary,
			WuiColors.HeroGradientMiddle,
			WuiColors.HeroGradientEnd,
		),
		start = Offset.Zero,
		end = Offset.Infinite,
	)
}