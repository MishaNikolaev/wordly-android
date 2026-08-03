package com.nmichail.wordly.android.component.ui.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

@Composable
fun AppCard(
	modifier: Modifier = Modifier,
	onClick: (() -> Unit)? = null,
	contentPadding: PaddingValues = PaddingValues(16.dp),
	content: @Composable ColumnScope.() -> Unit,
) {
	val shape = MaterialTheme.shapes.extraLarge
	val colors = CardDefaults.cardColors(
		containerColor = MaterialTheme.colorScheme.surface,
	)
	val elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
	val body: @Composable () -> Unit = {
		Column(
			modifier = Modifier.padding(contentPadding),
			content = content,
		)
	}

	if (onClick != null) {
		Card(
			onClick = onClick,
			modifier = modifier,
			shape = shape,
			colors = colors,
			elevation = elevation,
			content = { body() },
		)
	} else {
		Card(
			modifier = modifier,
			shape = shape,
			colors = colors,
			elevation = elevation,
			content = { body() },
		)
	}
}

@WordlyPreviews
@Composable
private fun AppCardPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		AppCard(
			modifier = Modifier.padding(16.dp),
			onClick = {},
		) {
			Text(text = "App card content")
		}
	}
}