package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

@Composable
fun CalendarStats(
	activeDaysCount: Int,
	currentStreak: Int,
	completionPercent: Int,
	modifier: Modifier = Modifier,
) {
	Row(modifier = modifier.fillMaxWidth()) {
		StatItem(
			value = activeDaysCount.toString(),
			label = stringResource(R.string.calendar_stat_days_in_month),
			modifier = Modifier.weight(1f),
		)
		StatItem(
			value = currentStreak.toString(),
			label = stringResource(R.string.calendar_stat_current_streak),
			modifier = Modifier.weight(1f),
		)
		StatItem(
			value = stringResource(R.string.calendar_stat_percent, completionPercent),
			label = stringResource(R.string.calendar_stat_completion),
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun StatItem(
	value: String,
	label: String,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = value,
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onSurface,
		)
		Text(
			text = label,
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 4.dp),
		)
	}
}
