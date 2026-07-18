package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

private val AuthSheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

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
					.background(color = surfaceColor, shape = AuthSheetShape),
			) {
				content()
			}
		}
	}
}
