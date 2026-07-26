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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object WeekDayStatusId {
	const val Completed = "completed"
	const val Today = "today"
	const val Missed = "missed"
	const val Upcoming = "upcoming"
}

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
			WeekDayStatusId.Today -> TodayDaySlot(dayOfMonth = dayOfMonth)
			WeekDayStatusId.Completed -> ActivityDaySlot(dayOfMonth = dayOfMonth)
			else -> PlainDaySlot(dayOfMonth = dayOfMonth)
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
private fun TodayDaySlot(dayOfMonth: Int) {
	val colorScheme = MaterialTheme.colorScheme
	DaySlotContainer {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Box(
				modifier = Modifier
					.size(36.dp)
					.background(colorScheme.primary, CircleShape),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = dayOfMonth.toString(),
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold,
					color = colorScheme.onPrimary,
				)
			}
		}
	}
}

@Composable
private fun ActivityDaySlot(dayOfMonth: Int) {
	val colorScheme = MaterialTheme.colorScheme
	DaySlotContainer {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Text(
				text = dayOfMonth.toString(),
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Medium,
				color = colorScheme.onSurface,
			)
			Box(
				modifier = Modifier
					.padding(top = 4.dp)
					.size(5.dp)
					.background(colorScheme.primary, CircleShape),
			)
		}
	}
}

@Composable
private fun PlainDaySlot(dayOfMonth: Int) {
	val colorScheme = MaterialTheme.colorScheme
	DaySlotContainer {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Text(
				text = dayOfMonth.toString(),
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Medium,
				color = colorScheme.onSurfaceVariant,
			)
			Box(
				modifier = Modifier
					.padding(top = 4.dp)
					.size(5.dp),
			)
		}
	}
}
