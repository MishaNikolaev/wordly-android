package com.nmichail.wordly.android.features.news.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.NewsAuthorRow
import com.nmichail.wordly.android.component.ui.components.NewsDetailActions
import com.nmichail.wordly.android.component.ui.components.NewsDetailHero
import com.nmichail.wordly.android.component.ui.components.NewsDetailTopBar
import com.nmichail.wordly.android.component.ui.components.NewsQuote
import com.nmichail.wordly.android.component.ui.components.TextLink
import com.nmichail.wordly.android.component.ui.components.snackbar.SnackBarHost
import com.nmichail.wordly.android.component.ui.components.snackbar.showErrorSnackBar
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import com.nmichail.wordly.android.features.news.R
import com.nmichail.wordly.android.features.news.domain.entity.NewsContentBlock
import com.nmichail.wordly.android.features.news.presentation.NewsDetailComponent

@Composable
fun NewsDetailContent(
	component: NewsDetailComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()
	val snackBarHostState = remember { SnackbarHostState() }

	NewsDetailLabelObserver(
		component = component,
		snackBarHostState = snackBarHostState,
	)

	Scaffold(
		modifier = modifier.fillMaxSize(),
		containerColor = MaterialTheme.colorScheme.background,
		contentWindowInsets = WindowInsets(0, 0, 0, 0),
		snackbarHost = { SnackBarHost(snackBarHostState = snackBarHostState) },
	) {
		NewsDetailBody(
			state = state,
			component = component,
			modifier = Modifier.fillMaxSize(),
		)
	}
}

@Composable
private fun NewsDetailLabelObserver(
	component: NewsDetailComponent,
	snackBarHostState: SnackbarHostState,
) {
	val context = LocalContext.current

	LaunchedEffect(component) {
		for (label in component.labelsChannel()) {
			when (label) {
				NewsDetailComponent.Label.Close -> Unit
				NewsDetailComponent.Label.NoConnection -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.news_detail_error_no_connection),
					)
				}
				NewsDetailComponent.Label.NotFound -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.news_detail_error_not_found),
					)
				}
				NewsDetailComponent.Label.UnknownError -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.news_detail_error_unknown),
					)
				}
			}
		}
	}
}

@Composable
private fun NewsDetailBody(
	state: NewsDetailComponent.State,
	component: NewsDetailComponent,
	modifier: Modifier = Modifier,
) {
	val context = LocalContext.current
	val colorScheme = MaterialTheme.colorScheme

	Box(modifier = modifier) {
		when (state) {
			NewsDetailComponent.State.Loading -> {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
					color = colorScheme.primary,
				)
			}
			NewsDetailComponent.State.Error -> {
				NewsDetailError(
					onBackClick = component::handleBack,
					onRetryClick = component::handleRetry,
					modifier = Modifier.fillMaxSize(),
				)
			}
			is NewsDetailComponent.State.Content -> {
				NewsDetailLoaded(
					state = state,
					onBackClick = component::handleBack,
					onShareClick = {
						shareNews(
							context = context,
							title = state.title,
							text = "${state.title}\n${state.subtitle}",
						)
					},
					onBookmarkClick = component::handleBookmark,
				)
			}
		}
	}
}

@Composable
private fun NewsDetailError(
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
			text = stringResource(R.string.news_detail_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.news_detail_error_description),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		Button(
			text = stringResource(R.string.news_detail_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
		TextLink(
			text = stringResource(ComponentR.string.news_detail_back),
			onClick = onBackClick,
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}

@Composable
private fun NewsDetailLoaded(
	state: NewsDetailComponent.State.Content,
	onBackClick: () -> Unit,
	onShareClick: () -> Unit,
	onBookmarkClick: () -> Unit,
) {
	val readingLabel = stringResource(
		ComponentR.string.news_detail_reading_minutes,
		state.readingMinutes,
	)

	NewsDetailScrollable(
		state = state,
		readingLabel = readingLabel,
		onBackClick = onBackClick,
		onShareClick = onShareClick,
		onBookmarkClick = onBookmarkClick,
		modifier = Modifier.fillMaxSize(),
	)
}

@Composable
private fun NewsDetailScrollable(
	state: NewsDetailComponent.State.Content,
	readingLabel: String,
	onBackClick: () -> Unit,
	onShareClick: () -> Unit,
	onBookmarkClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val authorMeta = stringResource(
		ComponentR.string.news_detail_author_meta,
		state.publishedAt,
		readingLabel,
	)

	Column(
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.navigationBarsPadding(),
	) {
		Box(modifier = Modifier.fillMaxWidth()) {
			NewsDetailHero(
				publishedAt = state.publishedAt,
				readingMinutesLabel = readingLabel,
				imageUrl = state.imageUrl,
			)
			NewsDetailTopBar(
				onBackClick = onBackClick,
				onBookmarkClick = onBookmarkClick,
				isBookmarked = state.isBookmarked,
				modifier = Modifier.align(Alignment.TopCenter),
			)
		}
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp)
				.padding(top = 20.dp, bottom = 24.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
		) {
			Text(
				text = stringResource(ComponentR.string.news_detail_category).uppercase(),
				style = WordlyTypography.newsDetailCategory,
				color = colorScheme.primary,
			)
			Text(
				text = state.title,
				style = WordlyTypography.newsDetailTitle,
				color = colorScheme.onSurface,
			)
			NewsAuthorRow(
				author = state.author,
				meta = authorMeta,
			)
			HorizontalDivider(
				thickness = 0.5.dp,
				color = colorScheme.outlineVariant,
			)
			NewsDetailBlocks(content = state.content)
			NewsDetailActions(onShareClick = onShareClick)
		}
	}
}

@Composable
private fun NewsDetailBlocks(
	content: List<NewsContentBlock>,
) {
	val colorScheme = MaterialTheme.colorScheme
	Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
		content.forEach { block ->
			when (block) {
				is NewsContentBlock.Paragraph -> {
					Text(
						text = block.text,
						style = MaterialTheme.typography.bodyLarge,
						color = colorScheme.onSurface,
					)
				}
				is NewsContentBlock.Quote -> {
					NewsQuote(text = block.text)
				}
			}
		}
	}
}

private fun shareNews(
	context: Context,
	title: String,
	text: String,
) {
	val intent = Intent(Intent.ACTION_SEND).apply {
		type = "text/plain"
		putExtra(Intent.EXTRA_SUBJECT, title)
		putExtra(Intent.EXTRA_TEXT, text)
	}
	context.startActivity(Intent.createChooser(intent, null))
}