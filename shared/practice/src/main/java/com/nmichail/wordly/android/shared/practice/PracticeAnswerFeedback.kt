package com.nmichail.wordly.android.shared.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews
import com.nmichail.wordly.android.component.wui.theme.Wui

@Composable
fun PracticeAnswerFeedback(
	isCorrect: Boolean,
	correctText: String,
	incorrectText: String,
	modifier: Modifier = Modifier,
	correctAnswerText: String? = null,
) {
	val color = if (isCorrect) {
		Wui.colors.success
	} else {
		MaterialTheme.colorScheme.error
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

@WuiPreviews
@Composable
private fun PracticeAnswerFeedbackPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
		) {
			PracticeAnswerFeedback(
				isCorrect = true,
				correctText = "Верно",
				incorrectText = "Неверно",
			)
			PracticeAnswerFeedback(
				isCorrect = false,
				correctText = "Верно",
				incorrectText = "Неверно",
				correctAnswerText = "correct",
			)
		}
	}
}
