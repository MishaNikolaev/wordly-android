package com.nmichail.wordly.android.features.review.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

internal val ReviewOptionShape = RoundedCornerShape(16.dp)
internal val ReviewColorBorder = WordlyColors.LightOutline.copy(alpha = 0.55f)

internal data class ReviewOptionColors(
	val background: Color,
	val text: Color,
)
