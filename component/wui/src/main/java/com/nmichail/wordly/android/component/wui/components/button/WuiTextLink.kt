package com.nmichail.wordly.android.component.wui.components.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews

@Composable
fun WuiTextLink(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	textAlign: TextAlign = TextAlign.Center,
	style: TextStyle = MaterialTheme.typography.labelLarge,
	color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
	TextButton(
		onClick = onClick,
		modifier = modifier,
		enabled = enabled,
	) {
		Text(
			text = text,
			style = style,
			color = if (enabled) color else color.copy(alpha = 0.4f),
			textAlign = textAlign,
		)
	}
}

@Composable
fun WuiCaptionText(
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

@WuiPreviews
@Composable
private fun TextLinkPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			WuiTextLink(text = "Уже есть аккаунт? Войти", onClick = {})
			WuiCaptionText(text = "или продолжите с email")
		}
	}
}
