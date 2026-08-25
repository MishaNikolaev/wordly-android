package com.nmichail.wordly.android.features.books.reader.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.pagination.wuiPaginateMeasuredTexts
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.books.reader.R
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.presentation.BookReaderStore
import com.nmichail.wordly.android.features.books.reader.ui.component.BookTranslatingOverlay

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun BookReaderPagedContent(
	state: BookReaderStore.State.Content,
	anchorParagraphIndex: Int,
	onAnchorParagraphIndexChange: (Int) -> Unit,
	pendingPage: Int,
	onPendingPageConsumed: () -> Unit,
	onPageInfoChange: (Int, Int) -> Unit,
	onToggleControls: () -> Unit,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val density = LocalDensity.current
	val textMeasurer = rememberTextMeasurer()
	val bodyStyle = WuiTypography.bookReaderBody
	val paragraphSpacingPx = with(density) { 16.dp.roundToPx() }
	val hintReservedPx = with(density) { 36.dp.roundToPx() }
	val horizontalPaddingPx = with(density) { 32.dp.roundToPx() }
	val verticalPaddingPx = with(density) { 16.dp.roundToPx() }

	BoxWithConstraints(modifier = modifier.fillMaxSize()) {
		val contentWidthPx = (constraints.maxWidth - horizontalPaddingPx).coerceAtLeast(0)
		val pageHeightPx =
			((constraints.maxHeight - verticalPaddingPx) * PAGE_CONTENT_HEIGHT_FACTOR)
				.toInt()
				.coerceAtLeast(0)

		val pages = rememberBookPages(
			paragraphs = state.book.paragraphs,
			translation = state.translation,
			translationVisible = state.translationVisible,
			contentWidthPx = contentWidthPx,
			pageHeightPx = pageHeightPx,
			paragraphSpacingPx = paragraphSpacingPx,
			hintReservedPx = hintReservedPx,
			bodyStyle = bodyStyle,
			measureHeight = { text, style, maxWidth ->
				textMeasurer.measure(
					text = text,
					style = style,
					constraints = Constraints(maxWidth = maxWidth),
				).size.height
			},
		)
		BookReaderHorizontalPager(
			state = state,
			pages = pages,
			anchorParagraphIndex = anchorParagraphIndex,
			onAnchorParagraphIndexChange = onAnchorParagraphIndexChange,
			pendingPage = pendingPage,
			onPendingPageConsumed = onPendingPageConsumed,
			onPageInfoChange = onPageInfoChange,
			onToggleControls = onToggleControls,
			onSelectWord = onSelectWord,
		)
	}
}

@Composable
private fun BookReaderHorizontalPager(
	state: BookReaderStore.State.Content,
	pages: List<IntRange>,
	anchorParagraphIndex: Int,
	onAnchorParagraphIndexChange: (Int) -> Unit,
	pendingPage: Int,
	onPendingPageConsumed: () -> Unit,
	onPageInfoChange: (Int, Int) -> Unit,
	onToggleControls: () -> Unit,
	onSelectWord: (String) -> Unit,
) {
	val pageCount = pages.size.coerceAtLeast(1)
	val pageForAnchor = pageIndexForParagraph(pages, anchorParagraphIndex)
	val pagerState = rememberPagerState(
		initialPage = pageForAnchor,
		pageCount = { pageCount },
	)
	var ignoreSettledAnchorUpdates by remember { mutableIntStateOf(0) }

	SyncPagerToAnchor(
		pages = pages,
		anchorParagraphIndex = anchorParagraphIndex,
		pageCount = pageCount,
		pagerState = pagerState,
		onPageInfoChange = onPageInfoChange,
		onIgnoreSettledChange = { delta -> ignoreSettledAnchorUpdates += delta },
	)
	SyncPagerToPendingPage(
		pendingPage = pendingPage,
		pages = pages,
		pageCount = pageCount,
		anchorParagraphIndex = anchorParagraphIndex,
		pagerState = pagerState,
		onAnchorParagraphIndexChange = onAnchorParagraphIndexChange,
		onPageInfoChange = onPageInfoChange,
		onPendingPageConsumed = onPendingPageConsumed,
		onIgnoreSettledChange = { delta -> ignoreSettledAnchorUpdates += delta },
	)
	SyncAnchorFromUserSwipe(
		pages = pages,
		pageCount = pageCount,
		anchorParagraphIndex = anchorParagraphIndex,
		ignoreSettledAnchorUpdates = ignoreSettledAnchorUpdates,
		pagerState = pagerState,
		onAnchorParagraphIndexChange = onAnchorParagraphIndexChange,
		onPageInfoChange = onPageInfoChange,
	)

	HorizontalPager(
		state = pagerState,
		modifier = Modifier.fillMaxSize(),
		userScrollEnabled = !state.translating,
	) { page ->
		val pageIndices = pages.getOrElse(page) { IntRange.EMPTY }
		BookReaderPage(
			showHint = page == 0,
			paragraphs = state.book.paragraphs,
			pageIndices = pageIndices,
			translation = state.translation,
			showTranslation = state.translationVisible,
			translating = state.translating,
			onToggleControls = onToggleControls,
			onSelectWord = onSelectWord,
		)
	}
}

@Composable
private fun SyncPagerToAnchor(
	pages: List<IntRange>,
	anchorParagraphIndex: Int,
	pageCount: Int,
	pagerState: PagerState,
	onPageInfoChange: (Int, Int) -> Unit,
	onIgnoreSettledChange: (Int) -> Unit,
) {
	LaunchedEffect(pages, anchorParagraphIndex) {
		val page = pageIndexForParagraph(pages, anchorParagraphIndex)
		onIgnoreSettledChange(1)
		try {
			if (pagerState.currentPage != page) {
				pagerState.scrollToPage(page)
			}
			onPageInfoChange(page, pageCount)
		} finally {
			onIgnoreSettledChange(-1)
		}
	}
}

@Composable
private fun SyncPagerToPendingPage(
	pendingPage: Int,
	pages: List<IntRange>,
	pageCount: Int,
	anchorParagraphIndex: Int,
	pagerState: PagerState,
	onAnchorParagraphIndexChange: (Int) -> Unit,
	onPageInfoChange: (Int, Int) -> Unit,
	onPendingPageConsumed: () -> Unit,
	onIgnoreSettledChange: (Int) -> Unit,
) {
	LaunchedEffect(pendingPage, pages) {
		if (pendingPage < 0) return@LaunchedEffect
		val safePage = pendingPage.coerceIn(0, pageCount - 1)
		onIgnoreSettledChange(1)
		try {
			if (pagerState.currentPage != safePage) {
				pagerState.scrollToPage(safePage)
			}
			pages.getOrNull(safePage)?.first?.let { paragraphIndex ->
				if (paragraphIndex != anchorParagraphIndex) {
					onAnchorParagraphIndexChange(paragraphIndex)
				}
			}
			onPageInfoChange(safePage, pageCount)
		} finally {
			onIgnoreSettledChange(-1)
			onPendingPageConsumed()
		}
	}
}

@Composable
private fun SyncAnchorFromUserSwipe(
	pages: List<IntRange>,
	pageCount: Int,
	anchorParagraphIndex: Int,
	ignoreSettledAnchorUpdates: Int,
	pagerState: PagerState,
	onAnchorParagraphIndexChange: (Int) -> Unit,
	onPageInfoChange: (Int, Int) -> Unit,
) {
	LaunchedEffect(pagerState.settledPage) {
		val settled = pagerState.settledPage.coerceIn(0, pageCount - 1)
		onPageInfoChange(settled, pageCount)
		if (ignoreSettledAnchorUpdates > 0) return@LaunchedEffect
		pages.getOrNull(settled)?.first?.let { paragraphIndex ->
			if (paragraphIndex != anchorParagraphIndex) {
				onAnchorParagraphIndexChange(paragraphIndex)
			}
		}
	}
}

@Composable
private fun BookReaderPage(
	showHint: Boolean,
	paragraphs: List<BookParagraph>,
	pageIndices: IntRange,
	translation: BookTranslation?,
	showTranslation: Boolean,
	translating: Boolean,
	onToggleControls: () -> Unit,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val scrollState = rememberScrollState()
	LaunchedEffect(pageIndices) {
		scrollState.scrollTo(0)
	}

	Box(modifier = modifier.fillMaxSize()) {
		if (translating) {
			BookTranslatingOverlay(
				message = stringResource(R.string.book_reader_translating),
			)
		} else {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.verticalScroll(scrollState)
					.padding(horizontal = 16.dp, vertical = 8.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp),
			) {
				if (showHint) {
					BookReaderHint()
				}
				pageIndices.forEach { index ->
					val paragraph = paragraphs[index]
					BookReaderParagraph(
						paragraph = paragraph,
						translatedText = translationFor(
							translation = translation,
							paragraphId = paragraph.id,
						),
						showTranslation = showTranslation,
						onSelectWord = onSelectWord,
						onContentTap = onToggleControls,
					)
				}
			}
		}
	}
}

@Composable
private fun BookReaderHint(
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Icon(
			imageVector = Icons.Rounded.TouchApp,
			contentDescription = null,
			tint = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.size(15.dp),
		)
		Text(
			text = stringResource(R.string.book_reader_hint),
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.padding(start = 6.dp),
		)
	}
}

@Composable
private fun rememberBookPages(
	paragraphs: List<BookParagraph>,
	translation: BookTranslation?,
	translationVisible: Boolean,
	contentWidthPx: Int,
	pageHeightPx: Int,
	paragraphSpacingPx: Int,
	hintReservedPx: Int,
	bodyStyle: TextStyle,
	measureHeight: (String, TextStyle, Int) -> Int,
): List<IntRange> =
	remember(
		paragraphs,
		translation,
		translationVisible,
		contentWidthPx,
		pageHeightPx,
		bodyStyle,
	) {
		if (contentWidthPx < MIN_PAGE_SIZE_PX || pageHeightPx < MIN_PAGE_SIZE_PX) {
			return@remember listOf(paragraphs.indices)
		}
		paginateBookParagraphs(
			paragraphs = paragraphs,
			translation = translation.takeIf { translationVisible },
			contentWidthPx = contentWidthPx,
			pageHeightPx = pageHeightPx,
			spacingPx = paragraphSpacingPx,
			firstPageReservedPx = hintReservedPx,
			style = bodyStyle,
			measure = measureHeight,
		)
	}

private fun pageIndexForParagraph(pages: List<IntRange>, paragraphIndex: Int): Int {
	if (pages.isEmpty()) return 0
	pages.indexOfFirst { paragraphIndex in it }.takeIf { it >= 0 }?.let { return it }
	pages.indexOfFirst { it.first == paragraphIndex }.takeIf { it >= 0 }?.let { return it }
	return 0
}

private fun paginateBookParagraphs(
	paragraphs: List<BookParagraph>,
	translation: BookTranslation?,
	contentWidthPx: Int,
	pageHeightPx: Int,
	spacingPx: Int,
	firstPageReservedPx: Int,
	style: TextStyle,
	measure: (String, TextStyle, Int) -> Int,
): List<IntRange> =
	wuiPaginateMeasuredTexts(
		primaryTexts = paragraphs.map { paragraph ->
			paragraph.segments.joinToString(separator = "") { it.text }
		},
		alternateTexts = paragraphs.map { paragraph ->
			translationFor(
				translation = translation,
				paragraphId = paragraph.id,
			)
		},
		contentWidthPx = contentWidthPx,
		pageHeightPx = pageHeightPx,
		spacingPx = spacingPx,
		firstPageReservedPx = firstPageReservedPx,
		style = style,
		measure = measure,
	)

private const val PAGE_CONTENT_HEIGHT_FACTOR = 1f
private const val MIN_PAGE_SIZE_PX = 48