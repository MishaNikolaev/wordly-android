package com.nmichail.wordly.android.component.wui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews

@Composable
fun WuiSectionLabel(
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

@WuiPreviews
@Composable
private fun SectionLabelPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		WuiSectionLabel(
			text = "На этой неделе",
			modifier = Modifier.padding(16.dp),
		)
	}
}
