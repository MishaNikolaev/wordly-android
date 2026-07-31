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
import com.nmichail.wordly.android.component.ui.theme.isAppInDarkTheme

private val DarkChipScrim = Color.Black.copy(alpha = 0.55f)

enum class StatusChipStyle {
	Neutral,
	Accent,
	Warm,
	Streak,
	OnMedia,
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
			.background(colors.background, RoundedCornerShape(percent = 50))
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
			fontWeight = if (style == StatusChipStyle.Accent) {
				FontWeight.SemiBold
			} else {
				FontWeight.Medium
			},
			color = colors.content,
		)
	}
}

@Composable
private fun chipColors(style: StatusChipStyle): ChipColors {
	val colorScheme = MaterialTheme.colorScheme
	val dark = isAppInDarkTheme()

	return when (style) {
		StatusChipStyle.Neutral -> ChipColors(
			background = if (dark) DarkChipScrim else colorScheme.surface,
			content = if (dark) colorScheme.onSurface else colorScheme.onSurfaceVariant,
			icon = if (dark) colorScheme.onSurface else colorScheme.onSurfaceVariant,
		)
		StatusChipStyle.Accent -> if (dark) {
			ChipColors(
				background = DarkChipScrim,
				content = colorScheme.onPrimaryContainer,
				icon = colorScheme.onPrimaryContainer,
			)
		} else {
			ChipColors(
				background = colorScheme.primaryContainer,
				content = colorScheme.onPrimaryContainer,
				icon = colorScheme.onPrimaryContainer,
			)
		}
		StatusChipStyle.Warm,
		StatusChipStyle.Streak,
		-> ChipColors(
			background = if (dark) {
				colorScheme.primaryContainer
			} else {
				colorScheme.primary.copy(alpha = 0.12f)
			},
			content = colorScheme.primary,
			icon = colorScheme.primary,
		)
		StatusChipStyle.OnMedia -> ChipColors(
			background = Color.White.copy(alpha = 0.22f),
			content = Color.White,
			icon = Color.White,
		)
	}
}

private data class ChipColors(
	val background: Color,
	val content: Color,
	val icon: Color,
)
