package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nmichail.wordly.android.component.ui.R

@Composable
fun CalendarDialog(
	monthTitle: String,
	days: List<CalendarDay?>,
	onDismiss: () -> Unit,
	onTodayClick: () -> Unit,
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
			onCloseClick = onDismiss,
			onDoneClick = onDismiss,
			onTodayClick = onTodayClick,
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
	onCloseClick: () -> Unit,
	onDoneClick: () -> Unit,
	onTodayClick: () -> Unit,
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
				.padding(horizontal = 20.dp, vertical = 16.dp),
		) {
			CalendarTopBar(
				onCloseClick = onCloseClick,
				onPreviousMonthClick = onPreviousMonthClick,
				onNextMonthClick = onNextMonthClick,
			)
			Spacer(modifier = Modifier.height(12.dp))
			Text(
				text = monthTitle,
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier.align(Alignment.CenterHorizontally),
			)
			Spacer(modifier = Modifier.height(16.dp))
			TextButton(
				onClick = onTodayClick,
				modifier = Modifier.align(Alignment.Start),
			) {
				Text(
					text = stringResource(R.string.calendar_today),
					style = MaterialTheme.typography.labelLarge,
					fontWeight = FontWeight.SemiBold,
					color = MaterialTheme.colorScheme.primary,
				)
			}
			Spacer(modifier = Modifier.height(4.dp))
			CalendarWeekdayLabels()
			Spacer(modifier = Modifier.height(8.dp))
			CalendarGrid(days = days)
			Spacer(modifier = Modifier.height(16.dp))
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
			) {
				TextButton(onClick = onDoneClick) {
					Text(
						text = stringResource(R.string.calendar_done),
						style = MaterialTheme.typography.labelLarge,
						fontWeight = FontWeight.Bold,
						color = MaterialTheme.colorScheme.primary,
					)
				}
			}
		}
	}
}
