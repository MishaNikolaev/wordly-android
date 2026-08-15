package com.nmichail.wordly.android.features.words.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.field.WuiSearchField
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.list.R
import com.nmichail.wordly.android.features.words.presentation.WordsComponent
import com.nmichail.wordly.android.features.words.presentation.WordsStore

@Composable
fun WordContent(
	component: WordsComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val current = state) {
		WordsStore.State.Loading -> WordsLoading(modifier = modifier)
		is WordsStore.State.Content -> WordsLoaded(
			state = current,
			component = component,
			modifier = modifier,
		)
		WordsStore.State.Error -> WordsError(
			onRetryClick = component::handleRetry,
			modifier = modifier.fillMaxSize(),
		)
	}
}

@Composable
private fun WordsLoading(modifier: Modifier = Modifier) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
		contentAlignment = Alignment.Center,
	) {
		CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
	}
}

@Composable
private fun WordsError(
	onRetryClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.background(MaterialTheme.colorScheme.background)
			.padding(horizontal = 24.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = stringResource(R.string.words_error_title),
			style = MaterialTheme.typography.titleLarge,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.words_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		WuiButton(
			text = stringResource(R.string.words_retry),
			onClick = onRetryClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
		)
	}
}

@Composable
private fun WordsLoaded(
	state: WordsStore.State.Content,
	component: WordsComponent,
	modifier: Modifier = Modifier,
) {
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	val clearSearchFocus = {
		focusManager.clearFocus(force = true)
		keyboardController?.hide()
		Unit
	}

	Box(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		state.wordDetailDialog?.let { dialog ->
			WordsDetailOverlay(dialog = dialog, component = component)
			return@Box
		}

		WordsMainContent(
			state = state,
			component = component,
			clearSearchFocus = clearSearchFocus,
			modifier = Modifier.fillMaxSize(),
		)
	}

	state.addWordDialog?.let { dialog ->
		AddWordDialog(
			state = dialog,
			onDismiss = component::handleDismissAddWord,
			onWordInputChange = component::handleAddWordInputChange,
			onToggleTag = component::handleToggleTag,
			onConfirm = component::handleConfirmAddWord,
		)
	}
}

@Composable
private fun WordsDetailOverlay(
	dialog: com.nmichail.wordly.android.features.words.presentation.WordDetailDialogState,
	component: WordsComponent,
) {
	WordDetailScreen(
		state = dialog,
		onDismiss = component::handleDismissWordDetail,
		onStatusChange = component::handleDetailStatusChange,
		onOpenCalendar = {
			component.handleCalendar(WordsStore.CalendarAction.Open)
		},
		onDismissCalendar = {
			component.handleCalendar(WordsStore.CalendarAction.Dismiss)
		},
		onCalendarPreviousMonth = {
			component.handleCalendar(WordsStore.CalendarAction.PreviousMonth)
		},
		onCalendarNextMonth = {
			component.handleCalendar(WordsStore.CalendarAction.NextMonth)
		},
		onCalendarToday = {
			component.handleCalendar(WordsStore.CalendarAction.Today)
		},
		onCalendarDayClick = { day ->
			component.handleCalendar(WordsStore.CalendarAction.DayClick(day))
		},
		onConfirmAddToReview = component::handleConfirmAddToReview,
		onPlayAudio = component::handlePlayAudio,
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	)
}

@Composable
private fun WordsMainContent(
	state: WordsStore.State.Content,
	component: WordsComponent,
	clearSearchFocus: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(modifier = modifier) {
		WordsScreenBody(
			state = state,
			onSearchQueryChange = component::handleSearchQueryChange,
			onFilterChange = { filter ->
				clearSearchFocus()
				component.handleFilterChange(filter)
			},
			onWordClick = { wordId ->
				clearSearchFocus()
				component.handleOpenWordDetail(wordId)
			},
			onScroll = clearSearchFocus,
		)
		FloatingActionButton(
			onClick = {
				clearSearchFocus()
				component.handleOpenAddWord()
			},
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(end = 16.dp, bottom = 16.dp),
			containerColor = MaterialTheme.colorScheme.primary,
			contentColor = MaterialTheme.colorScheme.onPrimary,
			shape = CircleShape,
		) {
			Icon(
				imageVector = Icons.Rounded.Add,
				contentDescription = stringResource(R.string.words_add_fab),
			)
		}
	}
}

@Composable
private fun WordsScreenBody(
	state: WordsStore.State.Content,
	onSearchQueryChange: (String) -> Unit,
	onFilterChange: (WordFilter) -> Unit,
	onWordClick: (String) -> Unit,
	onScroll: () -> Unit,
) {
	val listState = rememberLazyListState()
	LaunchedEffect(listState.isScrollInProgress) {
		if (listState.isScrollInProgress) {
			onScroll()
		}
	}

	LazyColumn(
		state = listState,
		modifier = Modifier.fillMaxSize(),
		contentPadding = PaddingValues(bottom = 88.dp),
		verticalArrangement = Arrangement.spacedBy(10.dp),
	) {
		wordsHeader(
			state = state,
			onSearchQueryChange = onSearchQueryChange,
			onFilterChange = onFilterChange,
		)
		wordsBody(
			words = state.words,
			onWordClick = onWordClick,
		)
	}
}

private fun LazyListScope.wordsHeader(
	state: WordsStore.State.Content,
	onSearchQueryChange: (String) -> Unit,
	onFilterChange: (WordFilter) -> Unit,
) {
	item(key = "title") {
		Text(
			text = state.title,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
		)
	}
	item(key = "search") {
		WuiSearchField(
			value = state.searchQuery,
			onValueChange = onSearchQueryChange,
			placeholder = state.searchPlaceholder.ifBlank {
				stringResource(R.string.words_search_placeholder)
			},
			modifier = Modifier.padding(horizontal = 16.dp),
		)
	}
	item(key = "filters") {
		WordsFilterChips(
			selected = state.selectedFilter,
			onSelect = onFilterChange,
			modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
		)
	}
}

private fun LazyListScope.wordsBody(
	words: List<WordItem>,
	onWordClick: (String) -> Unit,
) {
	if (words.isEmpty()) {
		item(key = "empty") {
			Text(
				text = stringResource(R.string.words_empty),
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 24.dp, vertical = 48.dp),
			)
		}
		return
	}
	items(
		items = words,
		key = { it.id },
	) { item ->
		WordListItem(
			item = item,
			onClick = { onWordClick(item.id) },
			modifier = Modifier.padding(horizontal = 16.dp),
		)
	}
}
