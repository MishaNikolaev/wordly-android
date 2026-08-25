package com.nmichail.wordly.android.features.books.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.component.wui.components.dialog.WuiSelectionDialog
import com.nmichail.wordly.android.component.wui.components.field.WuiSearchField
import com.nmichail.wordly.android.component.wui.R as ComponentR
import com.nmichail.wordly.android.features.books.domain.entity.BooksLevelBanner
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection
import com.nmichail.wordly.android.features.books.library.R
import com.nmichail.wordly.android.features.books.presentation.BooksComponent
import com.nmichail.wordly.android.features.books.presentation.BooksStore
@Composable
fun BooksContent(
    component: BooksComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()

    when (val uiState = state) {
        BooksStore.State.Initial,
        BooksStore.State.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        BooksStore.State.Error -> {
            BooksError(
                onRetryClick = component::handleRetry,
                onBackClick = component::handleBack,
                modifier = modifier.fillMaxSize(),
            )
        }

        is BooksStore.State.Content -> {
            BooksLoaded(
                state = uiState,
                component = component,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun BooksError(
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
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
            text = stringResource(R.string.books_error_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.books_error_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )
        WuiButton(
            text = stringResource(R.string.books_retry),
            onClick = onRetryClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )
        WuiTextLink(
            text = stringResource(R.string.books_back),
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun BooksLoaded(
    state: BooksStore.State.Content,
    component: BooksComponent,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        BooksCatalogList(
            state = state,
            onBackClick = component::handleBack,
            onLevelChange = component::handleLevelChange,
            onSearchQueryChange = component::handleSearchQueryChange,
            onBookClick = component::handleBookClick,
        )
    }
}

@Composable
private fun BooksCatalogList(
    state: BooksStore.State.Content,
    onBackClick: () -> Unit,
    onLevelChange: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "top_bar") {
            BooksTopBar(
                title = state.title,
                onBackClick = onBackClick,
            )
        }
        state.levelBanner?.let { banner ->
            item(key = "level_banner") {
                BooksLevelBannerContent(
                    banner = banner,
                    onLevelChange = onLevelChange,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
        item(key = "search") {
            WuiSearchField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = state.searchPlaceholder,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        booksSectionItems(
            sections = state.sections,
            onBookClick = onBookClick,
        )
    }
}

private fun LazyListScope.booksSectionItems(
    sections: List<BooksSection>,
    onBookClick: (String) -> Unit,
) {
    sections.forEach { section ->
        item(key = "section_${section.title}") {
            Text(
                text = section.title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 4.dp),
            )
        }
        items(
            items = section.items,
            key = { it.id },
        ) { item ->
            BooksListItem(
                item = item,
                onClick = { onBookClick(item.id) },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun BooksTopBar(
    title: String,
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
                contentDescription = stringResource(R.string.books_back),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun BooksLevelBannerContent(
    banner: BooksLevelBanner,
    onLevelChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = banner.text,
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onPrimaryContainer,
            modifier = Modifier.weight(1f),
        )
        BooksLevelSelector(
            selectedLevel = banner.levelLabel,
            levels = banner.levels.ifEmpty { listOf(banner.levelLabel) },
            onLevelChange = onLevelChange,
        )
    }
}

@Composable
private fun BooksLevelSelector(
    selectedLevel: String,
    levels: List<String>,
    onLevelChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(colorScheme.primary)
            .clickable { showDialog = true }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = selectedLevel,
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.onPrimary,
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = stringResource(R.string.books_level_banner_action),
            tint = colorScheme.onPrimary,
            modifier = Modifier.size(18.dp),
        )
    }

    if (showDialog) {
        WuiSelectionDialog(
            title = stringResource(R.string.books_level_dialog_title),
            options = levels,
            selectedOption = selectedLevel,
            saveButtonText = stringResource(ComponentR.string.common_ok),
            cancelButtonText = stringResource(ComponentR.string.common_cancel),
            onDismiss = { showDialog = false },
            onSave = { level ->
                showDialog = false
                if (level != selectedLevel) {
                    onLevelChange(level)
                }
            },
        )
    }
}