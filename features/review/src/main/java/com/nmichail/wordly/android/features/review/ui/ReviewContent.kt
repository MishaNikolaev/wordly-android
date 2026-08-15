package com.nmichail.wordly.android.features.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.features.review.R
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import com.nmichail.wordly.android.features.review.ui.component.ReviewInProgressContent
import com.nmichail.wordly.android.shared.practice.PracticeFinishedContent

@Composable
fun ReviewContent(
	component: ReviewComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val currentState = state) {
		ReviewComponent.State.Loading -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		ReviewComponent.State.Error -> {
			ReviewError(
				onRetryClick = component::handleRetry,
				onCloseClick = component::handleClose,
				modifier = modifier.fillMaxSize(),
			)
		}
		is ReviewComponent.State.InProgress -> {
			ReviewInProgressContent(
				state = currentState,
				component = component,
				modifier = modifier,
			)
		}
		is ReviewComponent.State.Finished -> {
			PracticeFinishedContent(
				correctCount = currentState.correctCount,
				totalCount = currentState.totalCount,
				subtitle = stringResource(
					R.string.review_finished_subtitle,
					currentState.correctCount,
					currentState.totalCount,
				),
				primaryActionText = stringResource(R.string.review_finished_home),
				onPrimaryClick = component::handleFinish,
				modifier = modifier,
			)
		}
	}
}

@Composable
private fun ReviewError(
	onRetryClick: () -> Unit,
	onCloseClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding()
			.padding(horizontal = 24.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = stringResource(R.string.review_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.review_error_description),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		WuiButton(
			text = stringResource(R.string.review_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
		WuiTextLink(
			text = stringResource(R.string.review_close_content_description),
			onClick = onCloseClick,
			modifier = Modifier.padding(top = 12.dp),
		)
	}
}