package com.nmichail.wordly.android.component.ui.components.text

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
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

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

@WordlyPreviews
@Composable
private fun ScreenTitlePreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		ScreenTitle(
			title = "Вход",
			subtitle = "Рады видеть вас снова",
			modifier = Modifier.padding(16.dp),
		)
	}
}