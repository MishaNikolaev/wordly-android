package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.component.ui.theme.isAppInDarkTheme

@Composable
fun PracticeAnswerFeedback(
	isCorrect: Boolean,
	correctText: String,
	incorrectText: String,
	modifier: Modifier = Modifier,
	correctAnswerText: String? = null,
) {
	val dark = isAppInDarkTheme()
	val color = if (isCorrect) {
		if (dark) WordlyColors.DarkSuccess else WordlyColors.LightSuccess
	} else {
		if (dark) WordlyColors.DarkError else WordlyColors.LightError
	}
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(6.dp),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
		) {
			Icon(
				imageVector = if (isCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
				contentDescription = null,
				tint = color,
				modifier = Modifier.size(22.dp),
			)
			Text(
				text = if (isCorrect) correctText else incorrectText,
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
				color = color,
			)
		}
		if (!isCorrect && !correctAnswerText.isNullOrBlank()) {
			Text(
				text = correctAnswerText,
				style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
				color = MaterialTheme.colorScheme.onSurface,
			)
		}
	}
}