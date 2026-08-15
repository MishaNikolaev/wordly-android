package com.nmichail.wordly.android.component.wui.components.calendar

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
import com.nmichail.wordly.android.component.wui.R
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews

@Composable
fun WuiCalendarDialog(
	monthTitle: String,
	days: List<WuiCalendarDay?>,
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
		WuiCalendar(
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
fun WuiCalendar(
	monthTitle: String,
	days: List<WuiCalendarDay?>,
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
			WuiCalendarTopBar(
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
			WuiCalendarWeekdayLabels()
			Spacer(modifier = Modifier.height(8.dp))
			WuiCalendarGrid(
				days = days,
				onDayClick = onDayClick,
			)
			Spacer(modifier = Modifier.height(16.dp))
			WuiCalendarDoneButton(onClick = onDoneClick)
		}
	}
}

@Composable
private fun WuiCalendarDoneButton(onClick: () -> Unit) {
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

@WuiPreviews
@Composable
private fun CalendarPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		val days = buildList {
			repeat(2) { add(null) }
			add(WuiCalendarDay(dayOfMonth = 1, statusId = WuiCalendarDayStatusId.Completed))
			add(WuiCalendarDay(dayOfMonth = 2, statusId = WuiCalendarDayStatusId.Missed))
			add(WuiCalendarDay(dayOfMonth = 3, statusId = WuiCalendarDayStatusId.Today))
			add(WuiCalendarDay(dayOfMonth = 4, statusId = WuiCalendarDayStatusId.Selected))
			add(WuiCalendarDay(dayOfMonth = 5, statusId = WuiCalendarDayStatusId.Inactive))
			repeat(37) { index ->
				add(WuiCalendarDay(dayOfMonth = index + 6, statusId = WuiCalendarDayStatusId.Inactive))
			}
		}.take(42)
		WuiCalendar(
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