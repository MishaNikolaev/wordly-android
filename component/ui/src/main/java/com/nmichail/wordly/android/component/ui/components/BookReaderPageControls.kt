package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val DISABLED_ALPHA = 0.4f

@Composable
fun BookReaderPageControls(
	currentPage: Int,
	pageCount: Int,
	onPreviousClick: () -> Unit,
	onNextClick: () -> Unit,
	previousContentDescription: String,
	nextContentDescription: String,
	modifier: Modifier = Modifier,
) {
	val safePageCount = pageCount.coerceAtLeast(1)
	val safeCurrent = currentPage.coerceIn(0, safePageCount - 1)
	val progress = (safeCurrent + 1).toFloat() / safePageCount.toFloat()

	Row(
		modifier = modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.surface)
			.padding(horizontal = 20.dp, vertical = 13.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(14.dp),
	) {
		BookReaderPageButton(
			enabled = safeCurrent > 0,
			onClick = onPreviousClick,
			contentDescription = previousContentDescription,
			icon = {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier.size(24.dp),
				)
			},
		)
		BookReaderPageProgress(
			progress = progress,
			label = "${safeCurrent + 1} / $safePageCount",
			modifier = Modifier.weight(1f),
		)
		BookReaderPageButton(
			enabled = safeCurrent < safePageCount - 1,
			onClick = onNextClick,
			contentDescription = nextContentDescription,
			icon = {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier.size(24.dp),
				)
			},
		)
	}
}

@Composable
private fun BookReaderPageProgress(
	progress: Float,
	label: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(7.dp),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(5.dp)
				.clip(RoundedCornerShape(percent = 50))
				.background(colorScheme.surfaceContainerHigh),
		) {
			Box(
				modifier = Modifier
					.fillMaxHeight()
					.fillMaxWidth(fraction = progress)
					.clip(RoundedCornerShape(percent = 50))
					.background(colorScheme.primary),
			)
		}
		Text(
			text = label,
			style = MaterialTheme.typography.labelMedium,
			fontWeight = FontWeight.Medium,
			color = colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.fillMaxWidth(),
		)
	}
}

@Composable
private fun BookReaderPageButton(
	enabled: Boolean,
	onClick: () -> Unit,
	contentDescription: String,
	icon: @Composable () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	val background = if (enabled) {
		colorScheme.surface
	} else {
		colorScheme.surfaceContainerHigh.copy(alpha = DISABLED_ALPHA)
	}

	Box(
		modifier = Modifier
			.size(44.dp)
			.clip(RoundedCornerShape(13.dp))
			.background(background)
			.then(
				if (enabled) {
					Modifier.border(
						width = 1.dp,
						color = colorScheme.outlineVariant,
						shape = RoundedCornerShape(13.dp),
					)
				} else {
					Modifier
				},
			)
			.clickable(
				enabled = enabled,
				role = Role.Button,
				onClickLabel = contentDescription,
				onClick = onClick,
			),
		contentAlignment = Alignment.Center,
	) {
		icon()
	}
}
