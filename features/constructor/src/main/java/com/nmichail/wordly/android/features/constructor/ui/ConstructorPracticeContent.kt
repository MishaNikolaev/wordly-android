package com.nmichail.wordly.android.features.constructor.ui

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
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.TextLink
import com.nmichail.wordly.android.features.constructor.R
import com.nmichail.wordly.android.features.constructor.presentation.detail.ConstructorPracticeComponent
import com.nmichail.wordly.android.features.constructor.ui.detail.ConstructorPracticeFinishedContent
import com.nmichail.wordly.android.features.constructor.ui.detail.ConstructorPracticeInProgressContent

@Composable
fun ConstructorPracticeContent(
	component: ConstructorPracticeComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val currentState = state) {
		ConstructorPracticeComponent.State.Loading -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		ConstructorPracticeComponent.State.Error -> {
			ConstructorPracticeError(
				onRetryClick = component::handleRetry,
				onCloseClick = component::handleClose,
				modifier = modifier.fillMaxSize(),
			)
		}
		is ConstructorPracticeComponent.State.InProgress -> {
			ConstructorPracticeInProgressContent(
				state = currentState,
				component = component,
				modifier = modifier,
			)
		}
		is ConstructorPracticeComponent.State.Finished -> {
			ConstructorPracticeFinishedContent(
				state = currentState,
				onBackClick = component::handleFinish,
				modifier = modifier,
			)
		}
	}
}

@Composable
private fun ConstructorPracticeError(
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
			text = stringResource(R.string.constructor_practice_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.constructor_practice_error_description),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		Button(
			text = stringResource(R.string.constructor_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
		TextLink(
			text = stringResource(R.string.constructor_practice_close),
			onClick = onCloseClick,
			modifier = Modifier.padding(top = 12.dp),
		)
	}
}