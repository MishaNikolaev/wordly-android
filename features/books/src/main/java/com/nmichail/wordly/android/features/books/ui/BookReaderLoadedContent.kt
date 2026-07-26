package com.nmichail.wordly.android.features.books.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.components.BookReaderPageControls
import com.nmichail.wordly.android.component.ui.components.BookTranslateButton
import com.nmichail.wordly.android.component.ui.components.BookTranslatingOverlay
import com.nmichail.wordly.android.component.ui.pagination.paginateMeasuredTexts
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import com.nmichail.wordly.android.features.books.R
import com.nmichail.wordly.android.features.books.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.presentation.detail.BookReaderComponent

@Composable
internal fun BookReaderLoaded(
	state: BookReaderComponent.State.Content,
	onCloseClick: () -> Unit,
	onToggleTranslate: () -> Unit,
	onSelectWord: (String) -> Unit,
	onDismissWordDialog: () -> Unit,
	onAddWordToCard: () -> Unit,
	onDismissWordAddedDialog: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(modifier = modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.statusBarsPadding()
				.navigationBarsPadding(),
		) {
			BookReaderTopBar(
				title = state.book.title,
				author = state.book.author,
				isTranslating = state.isTranslating,
				isTranslated = state.isTranslationVisible,
				onCloseClick = onCloseClick,
				onToggleTranslate = onToggleTranslate,
			)
			BookReaderBody(
				state = state,
				onSelectWord = onSelectWord,
				modifier = Modifier.weight(1f),
			)
		}
		val selectedWord = state.selectedWord
		if (selectedWord != null) {
			BookReaderWordLookupDialog(
				definition = selectedWord,
				isAdded = state.showWordAddedDialog,
				onAddClick = onAddWordToCard,
				onDismiss = if (state.showWordAddedDialog) {
					onDismissWordAddedDialog
				} else {
					onDismissWordDialog
				},
			)
		}
	}
}

@Composable
private fun BookReaderTopBar(
	title: String,
	author: String,
	isTranslating: Boolean,
	isTranslated: Boolean,
	onCloseClick: () -> Unit,
	onToggleTranslate: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		BookReaderBackButton(onClick = onCloseClick)
		Column(
			modifier = Modifier
				.weight(1f)
				.padding(horizontal = 12.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onBackground,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				textAlign = TextAlign.Center,
			)
			Text(
				text = author,
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				textAlign = TextAlign.Center,
			)
		}
		BookTranslateButton(
			isTranslating = isTranslating,
			isTranslated = isTranslated,
			contentDescription = stringResource(
				if (isTranslated) {
					R.string.book_reader_hide_translation
				} else {
					R.string.book_reader_translate
				},
			),
			onClick = onToggleTranslate,
		)
	}
}

@Composable
private fun BookReaderBackButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.size(40.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(MaterialTheme.colorScheme.surfaceVariant)
			.clickable(
				role = Role.Button,
				onClick = onClick,
			),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Filled.ArrowBack,
			contentDescription = stringResource(R.string.book_reader_close),
			tint = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.size(22.dp),
		)
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
private fun BookReaderBody(
	state: BookReaderComponent.State.Content,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	var currentPage by rememberSaveable { mutableIntStateOf(0) }
	var pageCount by rememberSaveable { mutableIntStateOf(1) }

	Column(modifier = modifier.fillMaxSize()) {
		BookReaderPagedContent(
			state = state,
			currentPage = currentPage,
			onPageCountChange = { count ->
				pageCount = count
				if (currentPage >= count) {
					currentPage = count - 1
				}
			},
			onSelectWord = onSelectWord,
			modifier = Modifier.weight(1f),
		)
		BookReaderPageControls(
			currentPage = currentPage,
			pageCount = pageCount,
			onPreviousClick = { currentPage = (currentPage - 1).coerceAtLeast(0) },
			onNextClick = { currentPage = (currentPage + 1).coerceAtMost(pageCount - 1) },
			previousContentDescription = stringResource(R.string.book_reader_previous_page),
			nextContentDescription = stringResource(R.string.book_reader_next_page),
		)
	}
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun BookReaderPagedContent(
	state: BookReaderComponent.State.Content,
	currentPage: Int,
	onPageCountChange: (Int) -> Unit,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val density = LocalDensity.current
	val textMeasurer = rememberTextMeasurer()
	val bodyStyle = WordlyTypography.bookReaderBody
	val paragraphSpacingPx = with(density) { 20.dp.roundToPx() }
	val hintReservedPx = with(density) { 28.dp.roundToPx() }
	val horizontalPaddingPx = with(density) { 32.dp.roundToPx() }
	val verticalPaddingPx = with(density) { 16.dp.roundToPx() }

	BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
		val contentWidthPx = (constraints.maxWidth - horizontalPaddingPx).coerceAtLeast(0)
		val pageHeightPx = (constraints.maxHeight - verticalPaddingPx).coerceAtLeast(0)
		val showTranslation = state.isTranslationVisible
		val pages = remember(
			state.book.paragraphs,
			state.translation,
			contentWidthPx,
			pageHeightPx,
			bodyStyle,
		) {
			paginateBookParagraphs(
				paragraphs = state.book.paragraphs,
				translation = state.translation,
				contentWidthPx = contentWidthPx,
				pageHeightPx = pageHeightPx,
				spacingPx = paragraphSpacingPx,
				firstPageReservedPx = hintReservedPx,
				style = bodyStyle,
				measure = { text, style, maxWidth ->
					textMeasurer.measure(
						text = text,
						style = style,
						constraints = Constraints(maxWidth = maxWidth),
					).size.height
				},
			)
		}
		val pageCount = pages.size.coerceAtLeast(1)
		LaunchedEffect(pageCount) {
			onPageCountChange(pageCount)
		}
		val pageIndices = pages.getOrElse(currentPage.coerceAtMost(pageCount - 1)) {
			IntRange.EMPTY
		}

		BookReaderPage(
			showHint = currentPage == 0,
			paragraphs = state.book.paragraphs,
			pageIndices = pageIndices,
			translation = state.translation,
			showTranslation = showTranslation,
			isTranslating = state.isTranslating,
			onSelectWord = onSelectWord,
		)
	}
}

@Composable
private fun BookReaderPage(
	showHint: Boolean,
	paragraphs: List<BookParagraph>,
	pageIndices: IntRange,
	translation: BookTranslation?,
	showTranslation: Boolean,
	isTranslating: Boolean,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(modifier = modifier.fillMaxSize()) {
		if (isTranslating) {
			BookTranslatingOverlay(
				message = stringResource(R.string.book_reader_translating),
			)
		} else {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(horizontal = 16.dp, vertical = 8.dp),
				verticalArrangement = Arrangement.spacedBy(20.dp),
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
					)
				}
			}
		}
	}
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
	paginateMeasuredTexts(
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