package com.nmichail.wordly.android.features.materials.article.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.features.materials.article.R
import com.nmichail.wordly.android.features.materials.article.presentation.MaterialDetailComponent
import com.nmichail.wordly.android.features.materials.article.presentation.MaterialDetailStore

@Composable
fun MaterialDetailContent(
	component: MaterialDetailComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	Box(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		when (val uiState = state) {
			MaterialDetailStore.State.Initial,
			MaterialDetailStore.State.Loading -> {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
					color = MaterialTheme.colorScheme.primary,
				)
			}

			MaterialDetailStore.State.Error -> MaterialDetailError(
				onBackClick = component::handleBack,
				onRetryClick = component::handleRetry,
				modifier = Modifier.fillMaxSize(),
			)

			is MaterialDetailStore.State.Content -> MaterialDetailLoaded(
				state = uiState,
				onBackClick = component::handleBack,
				onLikeClick = component::handleLike,
				onDislikeClick = component::handleDislike,
			)
		}
	}
}

@Composable
private fun MaterialDetailError(
	onBackClick: () -> Unit,
	onRetryClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.statusBarsPadding()
			.navigationBarsPadding()
			.padding(horizontal = 24.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = stringResource(R.string.materials_detail_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.materials_detail_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		WuiButton(
			text = stringResource(R.string.materials_detail_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
		WuiTextLink(
			text = stringResource(R.string.materials_detail_back),
			onClick = onBackClick,
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}

@Composable
private fun MaterialDetailLoaded(
	state: MaterialDetailStore.State.Content,
	onBackClick: () -> Unit,
	onLikeClick: () -> Unit,
	onDislikeClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.statusBarsPadding()
			.navigationBarsPadding()
			.verticalScroll(rememberScrollState())
			.padding(bottom = 24.dp),
	) {
		MaterialDetailTopBar(onBackClick = onBackClick)
		MaterialDetailHero(
			material = state.material,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
		)
		Text(
			text = stringResource(R.string.materials_detail_description),
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp),
		)
		Text(
			text = state.material.description,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
		)
		MaterialRatingSection(
			likes = state.material.likes,
			dislikes = state.material.dislikes,
			selectedReaction = state.selectedReaction,
			onLikeClick = onLikeClick,
			onDislikeClick = onDislikeClick,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
		)
	}
}

@Composable
private fun MaterialDetailTopBar(
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		IconButton(onClick = onBackClick) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = stringResource(R.string.materials_detail_back),
				tint = MaterialTheme.colorScheme.onBackground,
			)
		}
	}
}
