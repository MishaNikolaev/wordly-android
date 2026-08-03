package com.nmichail.wordly.android.component.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

@Composable
fun CalendarDayCell(
	day: CalendarDay,
	modifier: Modifier = Modifier,
	onClick: (() -> Unit)? = null,
) {
	val clickableModifier = if (onClick != null) {
		modifier
			.clip(CircleShape)
			.clickable(role = Role.Button, onClick = onClick)
	} else {
		modifier
	}

	when (day.statusId) {
		CalendarDayStatusId.Today -> TodayDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = clickableModifier,
		)
		CalendarDayStatusId.Selected -> SelectedDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = clickableModifier,
		)
		CalendarDayStatusId.Completed -> ActivityDayCell(
			dayOfMonth = day.dayOfMonth,
			modifier = clickableModifier,
		)
		else -> PlainDayCell(
			dayOfMonth = day.dayOfMonth,
			muted = day.statusId == CalendarDayStatusId.Inactive,
			modifier = clickableModifier,
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
private fun SelectedDayCell(
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
				.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
			contentAlignment = Alignment.Center,
		) {
			Text(
				text = dayOfMonth.toString(),
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.primary,
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

@WordlyPreviews
@Composable
private fun CalendarDayCellPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		Row(
			modifier = Modifier.padding(16.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
		) {
			listOf(
				CalendarDayStatusId.Today,
				CalendarDayStatusId.Selected,
				CalendarDayStatusId.Completed,
				CalendarDayStatusId.Missed,
				CalendarDayStatusId.Inactive,
			).forEachIndexed { index, status ->
				CalendarDayCell(
					day = CalendarDay(dayOfMonth = index + 1, statusId = status),
					onClick = {},
					modifier = Modifier.size(40.dp),
				)
			}
		}
	}
}