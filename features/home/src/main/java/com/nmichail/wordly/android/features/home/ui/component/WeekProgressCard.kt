package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.home.R
import com.nmichail.wordly.android.component.wui.components.card.WuiAppCard
import androidx.compose.ui.tooling.preview.Preview
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

@Composable
fun WeekProgressCard(
	onMonthClick: () -> Unit,
	modifier: Modifier = Modifier,
	daysContent: @Composable RowScope.() -> Unit,
) {
	WuiAppCard(modifier = modifier) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				text = stringResource(R.string.home_week_title),
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
			)
			Row(
				modifier = Modifier
					.clickable(onClick = onMonthClick)
					.padding(vertical = 4.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(2.dp),
			) {
				Text(
					text = stringResource(R.string.home_week_month_link),
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
				)
				Icon(
					imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier.size(18.dp),
				)
			}
		}
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			content = daysContent,
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun WeekProgressCardPreview() {
	WuiTheme {
		WeekProgressCard(
			onMonthClick = {},
			modifier = Modifier.padding(16.dp),
			daysContent = {
				listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEachIndexed { index, label ->
					WeekDayIndicator(
						label = label,
						statusId = if (index == 1) WeekDayStatusId.Today else WeekDayStatusId.Upcoming,
						dayOfMonth = index + 1,
						modifier = Modifier.weight(1f),
					)
				}
			},
		)
	}
}
