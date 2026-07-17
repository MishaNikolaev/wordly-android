package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CalendarDialog(
	monthTitle: String,
	days: List<CalendarDay?>,
	activeDaysCount: Int,
	currentStreak: Int,
	completionPercent: Int,
	onDismiss: () -> Unit,
	onPreviousMonthClick: () -> Unit,
	onNextMonthClick: () -> Unit,
) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false),
	) {
		Calendar(
			monthTitle = monthTitle,
			days = days,
			activeDaysCount = activeDaysCount,
			currentStreak = currentStreak,
			completionPercent = completionPercent,
			onCloseClick = onDismiss,
			onPreviousMonthClick = onPreviousMonthClick,
			onNextMonthClick = onNextMonthClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp),
		)
	}
}

@Composable
fun Calendar(
	monthTitle: String,
	days: List<CalendarDay?>,
	activeDaysCount: Int,
	currentStreak: Int,
	completionPercent: Int,
	onCloseClick: () -> Unit,
	onPreviousMonthClick: () -> Unit,
	onNextMonthClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Surface(
		modifier = modifier,
		shape = MaterialTheme.shapes.extraLarge,
		color = MaterialTheme.colorScheme.surface,
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp),
		) {
			CalendarTopBar(onCloseClick = onCloseClick)
			Spacer(modifier = Modifier.height(16.dp))
			CalendarMonthNavigation(
				monthTitle = monthTitle,
				onPreviousMonthClick = onPreviousMonthClick,
				onNextMonthClick = onNextMonthClick,
			)
			Spacer(modifier = Modifier.height(16.dp))
			CalendarWeekdayLabels()
			Spacer(modifier = Modifier.height(8.dp))
			CalendarGrid(days = days)
			Spacer(modifier = Modifier.height(20.dp))
			HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
			Spacer(modifier = Modifier.height(16.dp))
			CalendarLegend()
			Spacer(modifier = Modifier.height(20.dp))
			CalendarStats(
				activeDaysCount = activeDaysCount,
				currentStreak = currentStreak,
				completionPercent = completionPercent,
			)
		}
	}
}