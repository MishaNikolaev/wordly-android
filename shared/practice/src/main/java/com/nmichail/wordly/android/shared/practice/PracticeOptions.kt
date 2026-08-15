package com.nmichail.wordly.android.shared.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.wui.theme.Wui

private val PracticeOptionShape = RoundedCornerShape(16.dp)

private data class PracticeOptionColors(
	val background: Color,
	val text: Color,
)

@Composable
fun PracticeOptions(
	options: List<PracticeOption>,
	correctOptionId: String,
	selectedOptionId: String?,
	answerRevealed: Boolean,
	enabled: Boolean,
	onOptionClick: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(10.dp),
	) {
		options.forEach { option ->
			PracticeOptionButton(
				text = option.text,
				colors = optionColors(
					optionId = option.id,
					correctOptionId = correctOptionId,
					selectedOptionId = selectedOptionId,
					answerRevealed = answerRevealed,
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
	answerRevealed: Boolean,
): PracticeOptionColors {
	val colorScheme = MaterialTheme.colorScheme
	val extended = Wui.colors
	val successBackground = extended.successContainer
	val errorBackground = extended.errorContainer
	val selectedBackground = colorScheme.primaryContainer

	return when {
		!answerRevealed && optionId == selectedOptionId -> PracticeOptionColors(
			background = selectedBackground,
			text = colorScheme.onSurface,
		)
		!answerRevealed -> PracticeOptionColors(
			background = colorScheme.surface,
			text = colorScheme.onSurface,
		)
		optionId == correctOptionId -> PracticeOptionColors(
			background = successBackground,
			text = colorScheme.onSurface,
		)
		optionId == selectedOptionId -> PracticeOptionColors(
			background = errorBackground,
			text = colorScheme.onSurface,
		)
		else -> PracticeOptionColors(
			background = colorScheme.surface.copy(alpha = 0.6f),
			text = colorScheme.onSurface.copy(alpha = 0.45f),
		)
	}
}

@Composable
private fun PracticeOptionButton(
	text: String,
	colors: PracticeOptionColors,
	enabled: Boolean,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.clip(PracticeOptionShape)
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
