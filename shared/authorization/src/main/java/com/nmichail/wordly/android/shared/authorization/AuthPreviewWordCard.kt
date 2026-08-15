package com.nmichail.wordly.android.shared.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiTypography

@Composable
fun AuthPreviewWordCard(
	word: String,
	translation: String,
	modifier: Modifier = Modifier,
	rotation: Float = 0f,
) {
	val colorScheme = MaterialTheme.colorScheme

	Box(modifier = modifier.graphicsLayer { rotationZ = rotation }) {
		Column(
			modifier = Modifier
				.size(168.dp, 68.dp)
				.shadow(
					elevation = 20.dp,
					shape = RoundedCornerShape(20.dp),
					clip = false,
					ambientColor = Color.Black.copy(alpha = 0.38f),
					spotColor = Color.Black.copy(alpha = 0.38f),
				)
				.background(colorScheme.surface, RoundedCornerShape(20.dp))
				.padding(horizontal = 16.dp, vertical = 10.dp),
		) {
			Text(
				text = word,
				style = WuiTypography.authPreviewWord,
				color = colorScheme.onSurface,
			)
			Text(
				text = translation,
				style = WuiTypography.authPreviewTranslation,
				color = colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(top = 4.dp),
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun AuthPreviewWordCardPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		AuthPreviewWordCard(word = "serendipity", translation = "удача")
	}
}