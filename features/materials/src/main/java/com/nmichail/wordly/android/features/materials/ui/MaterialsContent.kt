package com.nmichail.wordly.android.features.materials.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.features.materials.R
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem
import com.nmichail.wordly.android.features.materials.presentation.MaterialsComponent
import com.nmichail.wordly.android.features.materials.presentation.MaterialsStore

@Composable
fun MaterialsContent(
    component: MaterialsComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()

    when (val uiState = state) {
        MaterialsStore.State.Initial,
        MaterialsStore.State.Loading -> MaterialsLoading(modifier = modifier)

        is MaterialsStore.State.Error -> MaterialsError(
            onRetryClick = component::handleRetry,
            modifier = modifier.fillMaxSize(),
        )

        is MaterialsStore.State.Content -> MaterialsLoaded(
            state = uiState,
            onFilterChange = component::handleFilterChange,
            onOpenMaterial = component::handleOpenMaterial,
            modifier = modifier,
        )
    }
}

@Composable
private fun MaterialsLoading(modifier: Modifier = Modifier) {
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
private fun MaterialsError(
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
            text = stringResource(R.string.materials_error_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.materials_error_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )
        WuiButton(
            text = stringResource(R.string.materials_retry),
            onClick = onRetryClick,
            modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
        )
    }
}

@Composable
private fun MaterialsLoaded(
    state: MaterialsStore.State.Content,
    onFilterChange: (MaterialFilter) -> Unit,
    onOpenMaterial: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            MaterialsFilterChips(
                selected = state.selectedFilter,
                onSelect = onFilterChange,
            )
        }
        if (state.items.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.materials_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 24.dp, vertical = 32.dp),
                )
            }
        } else {
            items(
                items = state.items,
                key = MaterialItem::id,
            ) { item ->
                MaterialListItem(
                    item = item,
                    onClick = { onOpenMaterial(item.id) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}
