package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(
	title: String,
	modifier: Modifier = Modifier,
	subtitle: String? = null,
	textAlign: TextAlign = TextAlign.Center,
) {
	Column(modifier = modifier.fillMaxWidth()) {
		Text(
			text = title,
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = textAlign,
			modifier = Modifier.fillMaxWidth(),
		)
		subtitle?.let { subtitleText ->
			Text(
				text = subtitleText,
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				textAlign = textAlign,
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 8.dp),
			)
		}
	}
}