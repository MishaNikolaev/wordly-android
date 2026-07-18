package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

private const val LEGEND_CORNER_DP = 4
private const val DASH_ON_DP = 3
private const val DASH_OFF_DP = 2
private const val STROKE_WIDTH_DP = 1.5f

@Composable
fun CalendarLegend(modifier: Modifier = Modifier) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		LegendItem(
			label = stringResource(R.string.calendar_legend_completed),
			swatch = { CompletedLegendSwatch() },
		)
		LegendItem(
			label = stringResource(R.string.calendar_legend_missed),
			swatch = { MissedLegendSwatch() },
		)
		LegendItem(
			label = stringResource(R.string.calendar_legend_today),
			swatch = { TodayLegendSwatch() },
		)
	}
}

@Composable
private fun LegendItem(
	label: String,
	swatch: @Composable () -> Unit,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(6.dp),
	) {
		swatch()
		Text(
			text = label,
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun CompletedLegendSwatch() {
	Box(
		modifier = Modifier
			.size(14.dp)
			.background(WordlyColors.LightPrimaryContainer, RoundedCornerShape(LEGEND_CORNER_DP.dp)),
	)
}

@Composable
private fun TodayLegendSwatch() {
	Box(
		modifier = Modifier
			.size(14.dp)
			.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(LEGEND_CORNER_DP.dp)),
	)
}

@Composable
private fun MissedLegendSwatch() {
	val outlineColor = MaterialTheme.colorScheme.outline
	Box(
		modifier = Modifier
			.size(14.dp)
			.drawBehind {
				val strokeWidth = STROKE_WIDTH_DP.dp.toPx()
				val corner = LEGEND_CORNER_DP.dp.toPx()
				drawRoundRect(
					color = outlineColor,
					topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
					size = Size(size.width - strokeWidth, size.height - strokeWidth),
					cornerRadius = CornerRadius(corner, corner),
					style = Stroke(
						width = strokeWidth,
						pathEffect = PathEffect.dashPathEffect(
							floatArrayOf(DASH_ON_DP.dp.toPx(), DASH_OFF_DP.dp.toPx()),
						),
					),
				)
			},
	)
}
