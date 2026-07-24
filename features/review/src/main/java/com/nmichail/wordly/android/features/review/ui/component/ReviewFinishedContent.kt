package com.nmichail.wordly.android.features.review.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.features.review.R
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent

@Composable
internal fun ReviewFinishedContent(
	state: ReviewComponent.State.Finished,
	onHomeClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding()
			.padding(horizontal = 24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ReviewFinishedResult(
			correctCount = state.correctCount,
			totalCount = state.totalCount,
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth(),
		)
		Button(
			text = stringResource(R.string.review_finished_home),
			onClick = onHomeClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 24.dp),
		)
	}
}

@Composable
private fun ReviewFinishedResult(
	correctCount: Int,
	totalCount: Int,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val isGreatResult = correctCount * 2 > totalCount
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Icon(
			imageVector = Icons.Rounded.CheckCircle,
			contentDescription = null,
			tint = if (isSystemInDarkTheme()) {
				WordlyColors.DarkSuccess
			} else {
				WordlyColors.LightSuccess
			},
			modifier = Modifier.size(72.dp),
		)
		Text(
			text = stringResource(
				if (isGreatResult) {
					R.string.review_finished_title_great
				} else {
					R.string.review_finished_title_done
				},
			),
			style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
			color = colorScheme.onSurface,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 20.dp),
		)
		Text(
			text = stringResource(R.string.review_finished_subtitle, correctCount, totalCount),
			style = MaterialTheme.typography.bodyLarge,
			color = colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
	}
}