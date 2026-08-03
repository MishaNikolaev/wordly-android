package com.nmichail.wordly.android.features.books.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.nmichail.wordly.android.features.books.ui.component.BookWordLookupDialog
import com.nmichail.wordly.android.component.ui.components.button.CustomButton
import com.nmichail.wordly.android.component.ui.components.button.TextLink
import com.nmichail.wordly.android.features.books.R
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.presentation.detail.BookReaderComponent

@Composable
fun BookReaderContent(
	component: BookReaderComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val currentState = state) {
		BookReaderComponent.State.Loading -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		BookReaderComponent.State.Error -> {
			BookReaderError(
				onRetryClick = component::handleRetry,
				onCloseClick = component::handleClose,
				modifier = modifier.fillMaxSize(),
			)
		}
		is BookReaderComponent.State.Content -> {
			BookReaderLoaded(
				state = currentState,
				onCloseClick = component::handleClose,
				onToggleTranslate = component::handleToggleTranslate,
				onSelectWord = component::handleSelectWord,
				onDismissWordDialog = component::handleDismissWordDialog,
				onAddWordToCard = component::handleAddWordToCard,
				onDismissWordAddedDialog = component::handleDismissWordAddedDialog,
				modifier = modifier,
			)
		}
	}
}

@Composable
private fun BookReaderError(
	onRetryClick: () -> Unit,
	onCloseClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.padding(horizontal = 24.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = stringResource(R.string.book_reader_error_title),
			style = MaterialTheme.typography.titleLarge,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.book_reader_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		CustomButton(
			text = stringResource(R.string.book_reader_retry),
			onClick = onRetryClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
		)
		TextLink(
			text = stringResource(R.string.book_reader_close),
			onClick = onCloseClick,
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}

@Composable
internal fun BookReaderWordLookupDialog(
	definition: BookWordDefinition,
	isAdded: Boolean,
	onAddClick: () -> Unit,
	onDismiss: () -> Unit,
) {
	BookWordLookupDialog(
		word = definition.word,
		phonetic = definition.phonetic,
		translation = definition.translation,
		addButtonText = stringResource(R.string.book_reader_add_word),
		addedStatusText = stringResource(R.string.book_reader_word_added_status),
		isAdded = isAdded,
		onAddClick = onAddClick,
		onDismiss = onDismiss,
	)
}