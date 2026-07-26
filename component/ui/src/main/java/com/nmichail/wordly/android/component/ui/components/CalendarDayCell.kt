package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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

@Composable
fun CalendarDayCell(
	day: CalendarDay,
	modifier: Modifier = Modifier,
) {
	when (day.statusId) {
		CalendarDayStatusId.Today -> TodayDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = modifier,
		)
		CalendarDayStatusId.Completed -> ActivityDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = modifier,
		)
		else -> PlainDayCell(
			dayOfMonth = day.dayOfMonth,
			muted = day.statusId == CalendarDayStatusId.Inactive,
			modifier = modifier,
		)
	}
}

@Composable
private fun TodayDayCell(
	dayOfMonth: Int,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.aspectRatio(1f),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Box(
			modifier = Modifier
				.size(36.dp)
				.background(MaterialTheme.colorScheme.primary, CircleShape),
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
}

@Composable
private fun ActivityDayCell(
	dayOfMonth: Int,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.aspectRatio(1f),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.Medium,
			color = MaterialTheme.colorScheme.onSurface,
		)
		Box(
			modifier = Modifier
				.padding(top = 4.dp)
				.size(5.dp)
				.background(MaterialTheme.colorScheme.primary, CircleShape),
		)
	}
}

@Composable
private fun PlainDayCell(
	dayOfMonth: Int,
	muted: Boolean,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.aspectRatio(1f),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Text(
			text = dayOfMonth.toString(),
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.Medium,
			color = if (muted) {
				MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
			} else {
				MaterialTheme.colorScheme.onSurface
			},
		)
		SpacerDot()
	}
}

@Composable
private fun SpacerDot() {
	Box(
		modifier = Modifier
			.padding(top = 4.dp)
			.size(5.dp),
	)
}
