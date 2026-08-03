package com.nmichail.wordly.android.component.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

@Composable
fun SectionLabel(
	text: String,
	modifier: Modifier = Modifier,
) {
	Text(
		text = text.uppercase(),
		modifier = modifier,
		style = MaterialTheme.typography.labelSmall,
		fontWeight = FontWeight.SemiBold,
		letterSpacing = 1.2.sp,
		color = MaterialTheme.colorScheme.onSurfaceVariant,
	)
}

@WordlyPreviews
@Composable
private fun SectionLabelPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		SectionLabel(
			text = "На этой неделе",
			modifier = Modifier.padding(16.dp),
		)
	}
}
