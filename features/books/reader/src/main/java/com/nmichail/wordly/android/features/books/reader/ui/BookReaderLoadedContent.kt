package com.nmichail.wordly.android.features.books.reader.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nmichail.wordly.android.features.books.reader.R
import com.nmichail.wordly.android.features.books.reader.presentation.BookReaderStore
import com.nmichail.wordly.android.features.books.reader.ui.component.BookReaderProgressBar
import com.nmichail.wordly.android.features.books.reader.ui.component.BookReaderTopBar

@Composable
internal fun BookReaderLoaded(
	state: BookReaderStore.State.Content,
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
				translating = state.translating,
				translated = state.translationVisible,
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
		if (state.wordLookupLoading) {
			BookWordLookupLoadingDialog(
				title = stringResource(R.string.book_reader_word_lookup_loading),
				onDismiss = onDismissWordDialog,
			)
		} else if (selectedWord != null) {
			BookReaderWordLookupDialog(
				definition = selectedWord,
				added = state.showWordAddedDialog,
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
private fun BookReaderBody(
	state: BookReaderStore.State.Content,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	var anchorParagraphIndex by rememberSaveable { mutableIntStateOf(0) }
	var controlsVisible by rememberSaveable { mutableStateOf(true) }
	var currentPage by rememberSaveable { mutableIntStateOf(0) }
	var pageCount by remember { mutableIntStateOf(1) }
	var pendingPage by remember { mutableIntStateOf(-1) }

	Column(modifier = modifier.fillMaxSize()) {
		BookReaderPagedContent(
			state = state,
			anchorParagraphIndex = anchorParagraphIndex,
			onAnchorParagraphIndexChange = { anchorParagraphIndex = it },
			pendingPage = pendingPage,
			onPendingPageConsumed = { pendingPage = -1 },
			onPageInfoChange = { page, count ->
				currentPage = page
				pageCount = count
			},
			onToggleControls = { controlsVisible = !controlsVisible },
			onSelectWord = onSelectWord,
			modifier = Modifier.weight(1f),
		)
		AnimatedVisibility(
			visible = controlsVisible,
			enter = fadeIn() + slideInVertically { it },
			exit = fadeOut() + slideOutVertically { it },
		) {
			BookReaderProgressBar(
				currentPage = currentPage,
				pageCount = pageCount,
				onPageChange = { page ->
					val safePage = page.coerceIn(0, (pageCount - 1).coerceAtLeast(0))
					currentPage = safePage
					pendingPage = safePage
				},
			)
		}
	}
}