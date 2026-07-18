package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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

object WeekDayStatusId {
	const val Completed = "completed"
	const val Today = "today"
	const val Missed = "missed"
	const val Upcoming = "upcoming"
}

internal const val DAY_SLOT_CORNER_DP = 10
private const val DASH_ON_DP = 5
private const val DASH_OFF_DP = 4
private const val STROKE_WIDTH_DP = 1.5f

private val DaySlotShape = RoundedCornerShape(DAY_SLOT_CORNER_DP.dp)

@Composable
fun WeekDayIndicator(
	label: String,
	statusId: String,
	dayOfMonth: Int,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
		when (statusId) {
			WeekDayStatusId.Completed -> CompletedDaySlot(dayOfMonth = dayOfMonth)
			WeekDayStatusId.Today -> TodayDaySlot(dayOfMonth = dayOfMonth)
			else -> OutlinedDaySlot(dayOfMonth = dayOfMonth)
		}
	}
}

@Composable
private fun DaySlotContainer(
	content: @Composable BoxScope.() -> Unit,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1f),
		contentAlignment = Alignment.Center,
		content = content,
	)
}

@Composable
private fun CompletedDaySlot(dayOfMonth: Int) {
	val colorScheme = MaterialTheme.colorScheme
	DaySlotContainer {
		Box(
			modifier = Modifier
				.matchParentSize()
				.background(colorScheme.primaryContainer, DaySlotShape),
		)
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Bold,
			color = Color.Black,
		)
		Box(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(2.dp)
				.size(12.dp)
				.background(colorScheme.primary, CircleShape),
			contentAlignment = Alignment.Center,
		) {
			Icon(
				imageVector = Icons.Filled.Check,
				contentDescription = null,
				tint = Color.Black,
				modifier = Modifier.size(8.dp),
			)
		}
	}
}

@Composable
private fun TodayDaySlot(dayOfMonth: Int) {
	val colorScheme = MaterialTheme.colorScheme
	DaySlotContainer {
		Box(
			modifier = Modifier
				.matchParentSize()
				.background(colorScheme.primary, DaySlotShape),
		)
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onPrimary,
		)
	}
}

@Composable
private fun OutlinedDaySlot(dayOfMonth: Int) {
	val outlineColor = MaterialTheme.colorScheme.outline
	val textColor = MaterialTheme.colorScheme.onSurfaceVariant
	DaySlotContainer {
		Box(
			modifier = Modifier
				.matchParentSize()
				.drawBehind {
					val strokeWidth = STROKE_WIDTH_DP.dp.toPx()
					val corner = DAY_SLOT_CORNER_DP.dp.toPx()
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
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Medium,
			color = textColor,
		)
	}
}
