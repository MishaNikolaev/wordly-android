@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

private val PillShape = RoundedCornerShape(percent = 50)

enum class StatusChipStyle {
	Neutral,
	Accent,
	Warm,
	Streak,
}

@Composable
fun StatusChip(
	text: String,
	modifier: Modifier = Modifier,
	icon: ImageVector? = null,
	style: StatusChipStyle = StatusChipStyle.Neutral,
) {
	val colors = chipColors(style)

	Row(
		modifier = modifier
			.background(colors.background, PillShape)
			.padding(horizontal = 10.dp, vertical = 6.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		if (icon != null) {
			Icon(
				imageVector = icon,
				contentDescription = null,
				tint = colors.icon,
				modifier = Modifier.size(14.dp),
			)
		}
		Text(
			text = text,
			style = MaterialTheme.typography.labelSmall,
			fontWeight = FontWeight.Medium,
			color = colors.content,
		)
	}
}

@Composable
private fun chipColors(style: StatusChipStyle): ChipColors {
	val colorScheme = MaterialTheme.colorScheme
	return when (style) {
		StatusChipStyle.Neutral -> ChipColors(
			background = colorScheme.surfaceVariant,
			content = colorScheme.onSurfaceVariant,
			icon = colorScheme.onSurfaceVariant,
		)
		StatusChipStyle.Accent -> ChipColors(
			background = WordlyColors.ReviewAccentContainer,
			content = WordlyColors.ReviewAccent,
			icon = WordlyColors.ReviewAccent,
		)
		StatusChipStyle.Warm,
		StatusChipStyle.Streak,
		-> ChipColors(
			background = WordlyColors.StreakContainer,
			content = WordlyColors.Streak,
			icon = WordlyColors.Streak,
		)
	}
}

private data class ChipColors(
	val background: Color,
	val content: Color,
	val icon: Color,
)
