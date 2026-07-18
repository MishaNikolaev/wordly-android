package com.nmichail.wordly.android.features.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.NewsAuthorRow
import com.nmichail.wordly.android.component.ui.components.NewsDetailActions
import com.nmichail.wordly.android.component.ui.components.NewsDetailHero
import com.nmichail.wordly.android.component.ui.components.NewsDetailTopBar
import com.nmichail.wordly.android.component.ui.components.NewsQuote
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import com.nmichail.wordly.android.features.news.domain.entity.NewsContentBlock
import com.nmichail.wordly.android.features.news.presentation.NewsDetailComponent

@Composable
fun NewsDetailContent(
	component: NewsDetailComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()
	val colorScheme = MaterialTheme.colorScheme

	Box(
		modifier = modifier
			.fillMaxSize()
			.background(colorScheme.background),
	) {
		if (state.isLoading) {
			CircularProgressIndicator(
				modifier = Modifier.align(Alignment.Center),
				color = colorScheme.primary,
			)
		} else {
			NewsDetailLoaded(
				state = state,
				onBackClick = component::handleBack,
				onShareClick = component::handleShare,
				onBookmarkClick = component::handleBookmark,
			)
		}
	}
}

@Composable
private fun NewsDetailLoaded(
	state: NewsDetailComponent.State,
	onBackClick: () -> Unit,
	onShareClick: () -> Unit,
	onBookmarkClick: () -> Unit,
) {
	val readingLabel = stringResource(
		ComponentR.string.news_detail_reading_minutes,
		state.readingMinutes,
	)

	Box(modifier = Modifier.fillMaxSize()) {
		NewsDetailScrollable(
			state = state,
			readingLabel = readingLabel,
			onShareClick = onShareClick,
			modifier = Modifier.fillMaxSize(),
		)
		NewsDetailTopBar(
			onBackClick = onBackClick,
			onBookmarkClick = onBookmarkClick,
			modifier = Modifier.align(Alignment.TopCenter),
		)
	}
}

@Composable
private fun NewsDetailScrollable(
	state: NewsDetailComponent.State,
	readingLabel: String,
	onShareClick: () -> Unit,
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
		NewsDetailHero(
			publishedAt = state.publishedAt,
			readingMinutesLabel = readingLabel,
			imageUrl = state.imageUrl,
		)
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