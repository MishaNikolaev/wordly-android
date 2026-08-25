package com.nmichail.wordly.android.features.books.reader.ui

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
import com.nmichail.wordly.android.features.books.reader.ui.component.BookWordLookupDialog
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.features.books.reader.R
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.reader.presentation.BookReaderComponent
import com.nmichail.wordly.android.features.books.reader.presentation.BookReaderStore

@Composable
fun BookReaderContent(
    component: BookReaderComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()

    when (val uiState = state) {
        BookReaderStore.State.Initial,
        BookReaderStore.State.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        BookReaderStore.State.Error -> {
            BookReaderError(
                onRetryClick = component::handleRetry,
                onCloseClick = component::handleClose,
                modifier = modifier.fillMaxSize(),
            )
        }

        is BookReaderStore.State.Content -> {
            BookReaderLoaded(
                state = uiState,
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
        WuiButton(
            text = stringResource(R.string.book_reader_retry),
            onClick = onRetryClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )
        WuiTextLink(
            text = stringResource(R.string.book_reader_close),
            onClick = onCloseClick,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
internal fun BookReaderWordLookupDialog(
    definition: BookWordDefinition,
    added: Boolean,
    onAddClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    BookWordLookupDialog(
        word = definition.word,
        phonetic = definition.phonetic,
        translation = definition.translation,
        definition = definition.definition,
        addButtonText = stringResource(R.string.book_reader_add_word),
        addedStatusText = stringResource(R.string.book_reader_word_added_status),
        noTranslationText = stringResource(R.string.book_reader_word_no_translation),
        added = added,
        onAddClick = onAddClick,
        onDismiss = onDismiss,
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
internal fun BookWordLookupLoadingDialog(
    title: String,
    onDismiss: () -> Unit,
) {
    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}