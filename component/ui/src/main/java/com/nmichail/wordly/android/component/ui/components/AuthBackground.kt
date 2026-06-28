package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Fill
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

private const val GRADIENT_PRIMARY_COLOR_STOP = 0.25f
private const val GRADIENT_PRIMARY_COLOR_ALPHA = 0.45f
private const val GRADIENT_BACKGROUND_COLOR_STOP = 0.36f
private const val GRADIENT_BACKGROUND_COLOR_ALPHA = 0.85f

@Composable
fun AuthBackground(
	modifier: Modifier = Modifier,
	header: @Composable () -> Unit,
	content: @Composable () -> Unit,
) {
	val backgroundColor = MaterialTheme.colorScheme.background
	val scrollState = rememberScrollState()

	Box(
		modifier = modifier.fillMaxSize(),
	) {
		Canvas(modifier = Modifier.matchParentSize()) {
			val colorStops = arrayOf(
				0f to WordlyColors.Primary,
				GRADIENT_PRIMARY_COLOR_STOP to WordlyColors.Primary.copy(
					alpha = GRADIENT_PRIMARY_COLOR_ALPHA,
				),
				GRADIENT_BACKGROUND_COLOR_STOP to backgroundColor.copy(
					alpha = GRADIENT_BACKGROUND_COLOR_ALPHA,
				),
				1f to backgroundColor,
			)
			val gradient = Brush.verticalGradient(
				colorStops = colorStops,
				startY = 0f,
				endY = size.height,
			)
			drawRect(color = backgroundColor, style = Fill)
			drawRect(brush = gradient, style = Fill)
		}
		Column(
			modifier = Modifier
				.fillMaxSize()
				.statusBarsPadding()
				.navigationBarsPadding()
				.verticalScroll(scrollState),
		) {
			header()
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.background(backgroundColor),
			) {
				content()
			}
		}
	}
}