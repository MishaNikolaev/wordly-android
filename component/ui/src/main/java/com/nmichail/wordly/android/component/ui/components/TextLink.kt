package com.nmichail.wordly.android.component.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TextLink(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	textAlign: TextAlign = TextAlign.Center,
	style: TextStyle = MaterialTheme.typography.labelLarge,
	color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
	TextButton(
		onClick = onClick,
		modifier = modifier,
	) {
		Text(
			text = text,
			style = style,
			color = color,
			textAlign = textAlign,
		)
	}
}

@Composable
fun CaptionText(
	text: String,
	modifier: Modifier = Modifier,
	textAlign: TextAlign = TextAlign.Center,
) {
	Text(
		text = text,
		modifier = modifier,
		style = MaterialTheme.typography.labelSmall,
		color = MaterialTheme.colorScheme.onSurfaceVariant,
		textAlign = textAlign,
	)
}