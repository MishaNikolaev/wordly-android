package com.nmichail.wordly.android.features.review.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.component.ui.theme.isAppInDarkTheme
import com.nmichail.wordly.android.features.review.domain.entity.ReviewOption

@Composable
internal fun ReviewOptions(
	options: List<ReviewOption>,
	correctOptionId: String,
	selectedOptionId: String?,
	isAnswerRevealed: Boolean,
	enabled: Boolean,
	onOptionClick: (String) -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(10.dp),
	) {
		options.forEach { option ->
			ReviewOptionButton(
				text = option.text,
				colors = optionColors(
					optionId = option.id,
					correctOptionId = correctOptionId,
					selectedOptionId = selectedOptionId,
					isAnswerRevealed = isAnswerRevealed,
				),
				enabled = enabled,
				onClick = { onOptionClick(option.id) },
			)
		}
	}
}

@Composable
private fun optionColors(
	optionId: String,
	correctOptionId: String,
	selectedOptionId: String?,
	isAnswerRevealed: Boolean,
): ReviewOptionColors {
	val colorScheme = MaterialTheme.colorScheme
	val dark = isAppInDarkTheme()
	val success = if (dark) WordlyColors.DarkSuccess else WordlyColors.LightSuccess
	val error = if (dark) WordlyColors.DarkError else WordlyColors.LightError
	val successBackground = success.copy(alpha = if (dark) 0.32f else 0.22f)
	val errorBackground = error.copy(alpha = if (dark) 0.32f else 0.22f)
	val selectedBackground = if (dark) {
		WordlyColors.DarkPrimaryContainer
	} else {
		WordlyColors.LightPrimaryContainer
	}

	return when {
		!isAnswerRevealed && optionId == selectedOptionId -> ReviewOptionColors(
			background = selectedBackground,
			text = colorScheme.onSurface,
		)
		!isAnswerRevealed -> ReviewOptionColors(
			background = colorScheme.surface,
			text = colorScheme.onSurface,
		)
		optionId == correctOptionId -> ReviewOptionColors(
			background = successBackground,
			text = colorScheme.onSurface,
		)
		optionId == selectedOptionId -> ReviewOptionColors(
			background = errorBackground,
			text = colorScheme.onSurface,
		)
		else -> ReviewOptionColors(
			background = colorScheme.surface.copy(alpha = 0.6f),
			text = colorScheme.onSurface.copy(alpha = 0.45f),
		)
	}
}

@Composable
private fun ReviewOptionButton(
	text: String,
	colors: ReviewOptionColors,
	enabled: Boolean,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.clip(ReviewOptionShape)
			.background(colors.background)
			.clickable(enabled = enabled, onClick = onClick)
			.padding(horizontal = 19.dp, vertical = 16.dp),
		contentAlignment = Alignment.CenterStart,
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.labelLarge.copy(
				fontWeight = FontWeight.SemiBold,
				fontSize = 16.sp,
				lineHeight = 19.2.sp,
			),
			color = colors.text,
		)
	}
}
