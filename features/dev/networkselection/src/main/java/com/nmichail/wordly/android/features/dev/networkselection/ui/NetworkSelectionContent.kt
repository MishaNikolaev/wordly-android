package com.nmichail.wordly.android.features.dev.networkselection.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.dev.networkselection.R
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand
import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionComponent
import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkSelectionContent(
	component: NetworkSelectionComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = {
					Text(text = stringResource(R.string.network_selection_title))
				},
				navigationIcon = {
					IconButton(onClick = component::handleNavigateBack) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = null,
						)
					}
				},
			)
		},
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
		) {
			NetworkSelectionList(
				state = state,
				onStandSelected = component::handleSelectStand,
			)
		}
	}
}

@Composable
private fun NetworkSelectionList(
	state: NetworkSelectionStore.State,
	onStandSelected: (NetworkStand) -> Unit,
) {
	val radioColors = RadioButtonDefaults.colors(
		selectedColor = MaterialTheme.colorScheme.primary,
		disabledSelectedColor = MaterialTheme.colorScheme.primary,
	)

	LazyColumn(modifier = Modifier.padding(36.dp)) {
		items(
			items = state.stands,
			key = NetworkStand::name,
		) { stand ->
			val selected = stand == state.selectedStand

			Row(verticalAlignment = Alignment.CenterVertically) {
				RadioButton(
					selected = selected,
					onClick = { onStandSelected(stand) },
					enabled = !selected,
					colors = radioColors,
				)
				Text(
					text = stand.name,
					style = MaterialTheme.typography.titleLarge,
				)
			}

			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}
