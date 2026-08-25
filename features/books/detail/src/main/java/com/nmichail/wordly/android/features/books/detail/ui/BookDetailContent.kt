package com.nmichail.wordly.android.features.books.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.component.wui.components.icon.WuiAnimatedToggleIcon
import com.nmichail.wordly.android.features.books.detail.R
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.detail.presentation.BookDetailComponent
import com.nmichail.wordly.android.features.books.detail.presentation.BookDetailStore
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.shared.catalog.CatalogRemoteImage
import com.nmichail.wordly.android.shared.catalog.rememberCoverGradientColors

@Composable
fun BookDetailContent(
	component: BookDetailComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()
	val colorScheme = MaterialTheme.colorScheme

	Box(
		modifier = modifier
			.fillMaxSize()
			.background(colorScheme.background),
	) {
		when (val uiState = state) {
			BookDetailStore.State.Initial,
			BookDetailStore.State.Loading -> {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
					color = colorScheme.primary,
				)
			}

			BookDetailStore.State.Error -> BookDetailError(
				onBackClick = component::handleBack,
				onRetryClick = component::handleRetry,
				onReadClick = component::handleRead,
				modifier = Modifier.fillMaxSize(),
			)

			is BookDetailStore.State.Content -> BookDetailLoaded(
				book = uiState.book,
				similarBooks = uiState.similarBooks,
				onBackClick = component::handleBack,
				onReadClick = component::handleRead,
				onSimilarBookClick = component::handleSimilarBookClick,
				modifier = Modifier.fillMaxSize(),
			)
		}
	}
}

@Composable
private fun BookDetailError(
	onBackClick: () -> Unit,
	onRetryClick: () -> Unit,
	onReadClick: () -> Unit,
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
			text = stringResource(R.string.book_detail_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.book_detail_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		WuiButton(
			text = stringResource(R.string.book_detail_read),
			onClick = onReadClick,
			modifier = Modifier.padding(top = 24.dp),
		)
		WuiButton(
			text = stringResource(R.string.book_detail_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 12.dp),
		)
		WuiTextLink(
			text = stringResource(R.string.book_detail_close),
			onClick = onBackClick,
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}

@Composable
private fun BookDetailLoaded(
	book: BookDetail,
	similarBooks: List<BooksItem>,
	onBackClick: () -> Unit,
	onReadClick: () -> Unit,
	onSimilarBookClick: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val isDark = colorScheme.background.luminance() < 0.5f
	val gradient = rememberCoverGradientColors(
		url = book.coverUrl,
		fallbackTop = colorScheme.surfaceVariant.copy(alpha = if (isDark) 0.45f else 0.55f),
		fallbackBottom = colorScheme.background,
		washToward = if (isDark) colorScheme.background else Color.White,
	)

	Box(
		modifier = modifier.background(colorScheme.background),
	) {
		BookDetailGradientBackdrop(
			top = gradient.top,
			bottom = gradient.bottom,
			background = colorScheme.background,
			modifier = Modifier
				.align(Alignment.TopCenter)
				.zIndex(0f),
		)
		BookDetailScrollContent(
			book = book,
			similarBooks = similarBooks,
			onBackClick = onBackClick,
			onReadClick = onReadClick,
			onSimilarBookClick = onSimilarBookClick,
			modifier = Modifier
				.fillMaxSize()
				.zIndex(1f),
		)
	}
}

@Composable
private fun BookDetailScrollContent(
	book: BookDetail,
	similarBooks: List<BooksItem>,
	onBackClick: () -> Unit,
	onReadClick: () -> Unit,
	onSimilarBookClick: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val scrollState = rememberScrollState()

	Column(
		modifier = modifier
			.statusBarsPadding()
			.navigationBarsPadding()
			.verticalScroll(scrollState)
			.padding(horizontal = 16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		BookDetailTopBar(
			onBackClick = onBackClick,
			modifier = Modifier.fillMaxWidth(),
		)
		BookDetailCover(
			coverUrl = book.coverUrl,
			modifier = Modifier.padding(top = 4.dp),
		)
		BookDetailInfo(
			book = book,
			modifier = Modifier.padding(top = 20.dp),
		)
		WuiButton(
			text = stringResource(R.string.book_detail_read),
			onClick = onReadClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp)
				.padding(top = 24.dp),
		)
		BookDetailSimilarSection(
			similarBooks = similarBooks,
			onSimilarBookClick = onSimilarBookClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp),
		)
		Spacer(modifier = Modifier.size(24.dp))
	}
}

@Composable
private fun BookDetailGradientBackdrop(
	top: Color,
	bottom: Color,
	background: Color,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(GRADIENT_HEIGHT)
			.background(
				Brush.verticalGradient(
					colorStops = arrayOf(
						0f to top,
						GRADIENT_MID_STOP to bottom,
						GRADIENT_FADE_STOP to background,
						1f to background,
					),
				),
			),
	)
}

@Composable
private fun BookDetailSimilarSection(
	similarBooks: List<BooksItem>,
	onSimilarBookClick: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	if (similarBooks.isEmpty()) return

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		Text(
			text = stringResource(R.string.book_detail_similar),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp),
		)
		similarBooks.chunked(SIMILAR_BOOKS_COLUMNS).forEach { rowItems ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(12.dp),
			) {
				rowItems.forEach { item ->
					BookDetailSimilarCard(
						item = item,
						onClick = { onSimilarBookClick(item.id) },
						modifier = Modifier
							.weight(1f)
							.padding(horizontal = 4.dp),
					)
				}
				if (rowItems.size < SIMILAR_BOOKS_COLUMNS) {
					Spacer(modifier = Modifier.weight(1f))
				}
			}
		}
	}
}

@Composable
private fun BookDetailInfo(
	book: BookDetail,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = book.title,
			style = MaterialTheme.typography.headlineMedium,
			color = colorScheme.onBackground,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(horizontal = 8.dp),
		)
		Text(
			text = book.author,
			style = MaterialTheme.typography.titleMedium,
			color = colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.padding(horizontal = 8.dp)
				.padding(top = 10.dp),
		)
		BookDetailTagsRow(
			genre = book.genre,
			category = book.category,
			level = book.level,
			modifier = Modifier.padding(top = 16.dp),
		)
		if (book.description.isNotBlank()) {
			Text(
				text = book.description,
				style = MaterialTheme.typography.bodyLarge,
				color = colorScheme.onSurface,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 8.dp)
					.padding(top = 20.dp),
			)
		}
	}
}

@Composable
private fun BookDetailTopBar(
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	var isImportant by rememberSaveable { mutableStateOf(false) }
	val colorScheme = MaterialTheme.colorScheme

	Row(
		modifier = modifier.padding(vertical = 4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		IconButton(onClick = onBackClick) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = stringResource(R.string.book_detail_close),
				tint = colorScheme.onBackground,
				modifier = Modifier.size(22.dp),
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		IconButton(
			onClick = {
				isImportant = !isImportant
				// TODO: сохранить книгу в коллекцию «Важное»
			},
		) {
			WuiAnimatedToggleIcon(
				checked = isImportant,
				checkedIcon = Icons.Filled.Bookmark,
				uncheckedIcon = Icons.Outlined.BookmarkBorder,
				contentDescription = stringResource(R.string.book_detail_add_to_important),
			)
		}
	}
}

@Composable
private fun BookDetailCover(
	coverUrl: String?,
	modifier: Modifier = Modifier,
) {
	val shape = RoundedCornerShape(6.dp)
	Box(
		modifier = modifier
			.width(COVER_WIDTH)
			.shadow(
				elevation = 6.dp,
				shape = shape,
				clip = false,
			)
			.clip(shape)
			.aspectRatio(COVER_ASPECT_RATIO)
			.background(MaterialTheme.colorScheme.surfaceContainerHigh),
	) {
		CatalogRemoteImage(
			url = coverUrl,
			modifier = Modifier.fillMaxSize(),
		)
	}
}

private const val COVER_ASPECT_RATIO = 2f / 3f
private const val GRADIENT_MID_STOP = 0.28f
private const val GRADIENT_FADE_STOP = 0.52f
private const val SIMILAR_BOOKS_COLUMNS = 2
private val COVER_WIDTH = 168.dp
private val GRADIENT_HEIGHT = 360.dp