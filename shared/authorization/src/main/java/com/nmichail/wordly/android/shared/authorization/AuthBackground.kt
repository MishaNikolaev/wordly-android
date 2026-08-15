package com.nmichail.wordly.android.shared.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider

@Composable
fun AuthBackground(
	modifier: Modifier = Modifier,
	header: @Composable () -> Unit,
	content: @Composable () -> Unit,
) {
	val surfaceColor = MaterialTheme.colorScheme.surface
	val scrollState = rememberScrollState()

	Box(
		modifier = modifier
			.fillMaxSize()
			.background(surfaceColor),
	) {
		Image(
			painter = painterResource(R.drawable.light),
			contentDescription = null,
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.TopCenter),
			contentScale = ContentScale.FillWidth,
		)
		Column(
			modifier = Modifier
				.fillMaxSize()
				.windowInsetsPadding(WindowInsets.safeDrawing.union(WindowInsets.ime))
				.verticalScroll(scrollState),
		) {
			header()
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.background(
						color = surfaceColor,
						shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
					),
			) {
				content()
			}
		}
	}
}

@Preview(showBackground = true, heightDp = 640)
@Composable
private fun AuthBackgroundPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		AuthBackground(
			header = {
				Text(
					text = "Welcome",
					style = MaterialTheme.typography.headlineMedium,
					color = Color.White,
					modifier = Modifier.padding(20.dp),
				)
			},
			content = {
				Text(
					text = "Sign in to continue",
					style = MaterialTheme.typography.bodyLarge,
					modifier = Modifier.padding(24.dp),
				)
			},
		)
	}
}