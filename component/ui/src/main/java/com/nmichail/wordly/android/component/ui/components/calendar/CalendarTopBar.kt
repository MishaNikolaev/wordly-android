package com.nmichail.wordly.android.component.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import androidx.compose.foundation.layout.padding
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

@Composable
fun CalendarTopBar(
	onCloseClick: () -> Unit,
	onPreviousMonthClick: () -> Unit,
	onNextMonthClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.size(34.dp)
				.clip(RoundedCornerShape(10.dp))
				.background(MaterialTheme.colorScheme.surfaceVariant)
				.clickable(role = Role.Button, onClick = onCloseClick),
			contentAlignment = Alignment.Center,
		) {
			Icon(
				imageVector = Icons.Filled.Close,
				contentDescription = stringResource(R.string.calendar_close),
				tint = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.size(20.dp),
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
			CalendarNavIconButton(
				onClick = onPreviousMonthClick,
				contentDescription = stringResource(R.string.calendar_previous_month),
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
			)
			CalendarNavIconButton(
				onClick = onNextMonthClick,
				contentDescription = stringResource(R.string.calendar_next_month),
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
			)
		}
	}
}

@Composable
private fun CalendarNavIconButton(
	onClick: () -> Unit,
	contentDescription: String,
	imageVector: ImageVector,
) {
	Box(
		modifier = Modifier
			.size(40.dp)
			.clip(CircleShape)
			.clickable(onClick = onClick),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			imageVector = imageVector,
			contentDescription = contentDescription,
			tint = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.size(28.dp),
		)
	}
}

@WordlyPreviews
@Composable
private fun CalendarTopBarPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		CalendarTopBar(
			onCloseClick = {},
			onPreviousMonthClick = {},
			onNextMonthClick = {},
			modifier = Modifier.padding(8.dp),
		)
	}
}