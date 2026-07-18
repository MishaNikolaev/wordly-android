package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

@Composable
fun CalendarTopBar(
	onCloseClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = stringResource(R.string.calendar_title),
			style = WordlyTypography.homeScreenTitle,
			color = MaterialTheme.colorScheme.onSurface,
		)
		CircularIconButton(onClick = onCloseClick) {
			Icon(
				imageVector = Icons.Filled.Close,
				contentDescription = stringResource(R.string.calendar_close),
				tint = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.size(18.dp),
			)
		}
	}
}

@Composable
fun CalendarMonthNavigation(
	monthTitle: String,
	onPreviousMonthClick: () -> Unit,
	onNextMonthClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		CircularIconButton(onClick = onPreviousMonthClick) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
				contentDescription = stringResource(R.string.calendar_previous_month),
				tint = MaterialTheme.colorScheme.onSurfaceVariant,
			)
		}
		Text(
			text = monthTitle,
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onSurface,
		)
		CircularIconButton(onClick = onNextMonthClick) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
				contentDescription = stringResource(R.string.calendar_next_month),
				tint = MaterialTheme.colorScheme.onSurfaceVariant,
			)
		}
	}
}

@Composable
internal fun CircularIconButton(
	onClick: () -> Unit,
	content: @Composable () -> Unit,
) {
	Box(
		modifier = Modifier
			.size(36.dp)
			.clip(CircleShape)
			.background(MaterialTheme.colorScheme.surfaceVariant)
			.clickable(onClick = onClick),
		contentAlignment = Alignment.Center,
	) {
		content()
	}
}
