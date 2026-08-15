package com.nmichail.wordly.android.component.wui.components.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.R
import androidx.compose.foundation.layout.padding
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews

internal const val DAYS_IN_WEEK = 7

@Composable
fun WuiCalendarWeekdayLabels(modifier: Modifier = Modifier) {
	val labels = listOf(
		stringResource(R.string.calendar_day_mon),
		stringResource(R.string.calendar_day_tue),
		stringResource(R.string.calendar_day_wed),
		stringResource(R.string.calendar_day_thu),
		stringResource(R.string.calendar_day_fri),
		stringResource(R.string.calendar_day_sat),
		stringResource(R.string.calendar_day_sun),
	)
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(6.dp),
	) {
		labels.forEach { label ->
			Text(
				text = label,
				modifier = Modifier.weight(1f),
				style = MaterialTheme.typography.labelSmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				textAlign = TextAlign.Center,
			)
		}
	}
}

@Composable
fun WuiCalendarGrid(
	days: List<WuiCalendarDay?>,
	modifier: Modifier = Modifier,
	onDayClick: ((Int) -> Unit)? = null,
) {
	Column(
		modifier = modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(6.dp),
	) {
		days.chunked(DAYS_IN_WEEK).forEach { week ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(6.dp),
			) {
				week.forEach { day ->
					Box(modifier = Modifier.weight(1f)) {
						if (day != null) {
							val isSelectable = day.statusId != WuiCalendarDayStatusId.Inactive
							WuiCalendarDayCell(
								day = day,
								onClick = onDayClick
									?.takeIf { isSelectable }
									?.let { click ->
										{ click(day.dayOfMonth) }
									},
							)
						}
					}
				}
				repeat(DAYS_IN_WEEK - week.size) {
					Spacer(modifier = Modifier.weight(1f))
				}
			}
		}
	}
}

@WuiPreviews
@Composable
private fun CalendarGridPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		val days = List(42) { index ->
			val day = index - 1
			if (day in 1..31) {
				WuiCalendarDay(
					dayOfMonth = day,
					statusId = when (day) {
						3 -> WuiCalendarDayStatusId.Today
						5 -> WuiCalendarDayStatusId.Completed
						7 -> WuiCalendarDayStatusId.Missed
						else -> WuiCalendarDayStatusId.Inactive
					},
				)
			} else {
				null
			}
		}
		Column(modifier = Modifier.padding(16.dp)) {
			WuiCalendarWeekdayLabels()
			WuiCalendarGrid(days = days, onDayClick = {})
		}
	}
}