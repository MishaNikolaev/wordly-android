package com.nmichail.wordly.android.component.ui.components

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
import com.nmichail.wordly.android.component.ui.R

internal const val DAYS_IN_WEEK = 7

@Composable
fun CalendarWeekdayLabels(modifier: Modifier = Modifier) {
	val labels = listOf(
		stringResource(R.string.home_day_mon),
		stringResource(R.string.home_day_tue),
		stringResource(R.string.home_day_wed),
		stringResource(R.string.home_day_thu),
		stringResource(R.string.home_day_fri),
		stringResource(R.string.home_day_sat),
		stringResource(R.string.home_day_sun),
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
fun CalendarGrid(
	days: List<CalendarDay?>,
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
							val isSelectable = day.statusId != CalendarDayStatusId.Inactive
							CalendarDayCell(
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
