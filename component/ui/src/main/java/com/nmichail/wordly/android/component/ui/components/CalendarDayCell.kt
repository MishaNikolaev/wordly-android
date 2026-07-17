package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

private val DayCellShape = RoundedCornerShape(10.dp)
private const val DAY_CELL_CORNER_DP = 10
private const val DASH_ON_DP = 4
private const val DASH_OFF_DP = 3
private const val STROKE_WIDTH_DP = 1.5f

@Composable
fun CalendarDayCell(
	day: CalendarDay,
	modifier: Modifier = Modifier,
) {
	when (day.statusId) {
		CalendarDayStatusId.Completed -> CompletedDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = modifier,
		)
		CalendarDayStatusId.Missed -> MissedDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = modifier,
		)
		CalendarDayStatusId.Today -> TodayDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = modifier,
		)
		else -> InactiveDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = modifier,
		)
	}
}

@Composable
private fun CompletedDayCell(
	dayOfMonth: Int,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.aspectRatio(1f)
			.background(WordlyColors.LightPrimaryContainer, DayCellShape),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.SemiBold,
			color = WordlyColors.ReviewAccent,
		)
		Box(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(2.dp)
				.size(10.dp)
				.background(MaterialTheme.colorScheme.primary, CircleShape),
			contentAlignment = Alignment.Center,
		) {
			Icon(
				imageVector = Icons.Filled.Check,
				contentDescription = null,
				tint = Color.Black,
				modifier = Modifier.size(7.dp),
			)
		}
	}
}

@Composable
private fun TodayDayCell(
	dayOfMonth: Int,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.aspectRatio(1f)
			.background(MaterialTheme.colorScheme.primary, DayCellShape),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onPrimary,
		)
	}
}

@Composable
private fun MissedDayCell(
	dayOfMonth: Int,
	modifier: Modifier = Modifier,
) {
	val outlineColor = MaterialTheme.colorScheme.outline
	Box(
		modifier = modifier
			.aspectRatio(1f)
			.drawBehind {
				val strokeWidth = STROKE_WIDTH_DP.dp.toPx()
				val corner = DAY_CELL_CORNER_DP.dp.toPx()
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
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.Medium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun InactiveDayCell(
	dayOfMonth: Int,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.aspectRatio(1f)
			.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.Medium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}
