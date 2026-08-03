package com.nmichail.wordly.android.component.ui.components.calendar

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
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

@Composable
fun CalendarDialog(
	monthTitle: String,
	days: List<CalendarDay?>,
	onDismiss: () -> Unit,
	onTodayClick: () -> Unit,
	onPreviousMonthClick: () -> Unit,
	onNextMonthClick: () -> Unit,
	onDayClick: ((Int) -> Unit)? = null,
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
			onDayClick = onDayClick,
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
	onDayClick: ((Int) -> Unit)? = null,
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
			CalendarGrid(
				days = days,
				onDayClick = onDayClick,
			)
			Spacer(modifier = Modifier.height(16.dp))
			CalendarDoneButton(onClick = onDoneClick)
		}
	}
}

@Composable
private fun CalendarDoneButton(onClick: () -> Unit) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.End,
	) {
		TextButton(onClick = onClick) {
			Text(
				text = stringResource(R.string.calendar_done),
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.primary,
			)
		}
	}
}

@WordlyPreviews
@Composable
private fun CalendarPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		val days = buildList {
			repeat(2) { add(null) }
			add(CalendarDay(dayOfMonth = 1, statusId = CalendarDayStatusId.Completed))
			add(CalendarDay(dayOfMonth = 2, statusId = CalendarDayStatusId.Missed))
			add(CalendarDay(dayOfMonth = 3, statusId = CalendarDayStatusId.Today))
			add(CalendarDay(dayOfMonth = 4, statusId = CalendarDayStatusId.Selected))
			add(CalendarDay(dayOfMonth = 5, statusId = CalendarDayStatusId.Inactive))
			repeat(37) { index ->
				add(CalendarDay(dayOfMonth = index + 6, statusId = CalendarDayStatusId.Inactive))
			}
		}.take(42)
		Calendar(
			monthTitle = "Август 2026",
			days = days,
			onCloseClick = {},
			onDoneClick = {},
			onTodayClick = {},
			onPreviousMonthClick = {},
			onNextMonthClick = {},
			onDayClick = {},
		)
	}
}