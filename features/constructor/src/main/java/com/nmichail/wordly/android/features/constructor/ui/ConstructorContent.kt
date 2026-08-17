package com.nmichail.wordly.android.features.constructor.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.card.WuiCatalogCard
import com.nmichail.wordly.android.component.wui.components.field.WuiSearchField
import com.nmichail.wordly.android.component.wui.components.text.WuiSectionLabel
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.features.constructor.R
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorLevelBanner
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorComponent
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorStore
import com.nmichail.wordly.android.shared.catalog.CatalogRemoteImage

@Composable
fun ConstructorContent(
	component: ConstructorComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val currentState = state) {
		ConstructorStore.State.Initial -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		ConstructorStore.State.Loading -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		ConstructorStore.State.Error -> {
			ConstructorError(
				onRetryClick = component::handleRetry,
				onBackClick = component::handleBack,
				modifier = modifier.fillMaxSize(),
			)
		}
		is ConstructorStore.State.Content -> {
			ConstructorLoaded(
				state = currentState,
				component = component,
				modifier = modifier,
			)
		}
	}
}

@Composable
private fun ConstructorError(
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
			text = stringResource(R.string.constructor_error_title),
			style = MaterialTheme.typography.titleLarge,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.constructor_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		WuiButton(
			text = stringResource(R.string.constructor_retry),
			onClick = onRetryClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
		)
		WuiTextLink(
			text = stringResource(R.string.constructor_back),
			onClick = onBackClick,
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}

@Composable
private fun ConstructorLoaded(
	state: ConstructorStore.State.Content,
	component: ConstructorComponent,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding(),
	) {
		ConstructorTopBar(
			title = state.title,
			onBackClick = component::handleBack,
		)
		LazyColumn(
			modifier = Modifier.fillMaxSize(),
			contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			state.levelBanner?.let { banner ->
				item(key = "level_banner") {
					ConstructorLevelBannerContent(
						banner = banner,
						onLevelChange = component::handleLevelChange,
					)
				}
			}
			item(key = "search") {
				WuiSearchField(
					value = state.searchQuery,
					onValueChange = component::handleSearchQueryChange,
					placeholder = state.searchPlaceholder,
				)
			}
			state.sections.forEach { section ->
				item(key = "section_${section.title}") {
					WuiSectionLabel(
						text = section.title,
						modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
					)
				}
				items(
					items = section.items,
					key = { it.id },
				) { item ->
					WuiCatalogCard(
						title = item.title,
						subtitle = item.subtitle,
						badge = item.badge,
						image = { CatalogRemoteImage(url = item.imageUrl) },
						onClick = { component.handleThemeClick(item.id) },
					)
				}
			}
		}
	}
}

@Composable
private fun ConstructorTopBar(
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
				contentDescription = stringResource(R.string.constructor_back),
				tint = MaterialTheme.colorScheme.onBackground,
			)
		}
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.padding(start = 4.dp),
		)
	}
}

@Composable
private fun ConstructorLevelBannerContent(
	banner: ConstructorLevelBanner,
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
		ConstructorLevelSelector(
			selectedLevel = banner.levelLabel,
			levels = banner.levels.ifEmpty { listOf(banner.levelLabel) },
			onLevelChange = onLevelChange,
		)
	}
}

@Composable
private fun ConstructorLevelSelector(
	selectedLevel: String,
	levels: List<String>,
	onLevelChange: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	var expanded by remember { mutableStateOf(false) }

	Box(modifier = modifier) {
		Row(
			modifier = Modifier
				.clip(MaterialTheme.shapes.extraLarge)
				.background(colorScheme.primary)
				.clickable { expanded = true }
				.padding(horizontal = 12.dp, vertical = 8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(2.dp),
		) {
			Text(
				text = selectedLevel,
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.SemiBold,
				color = colorScheme.onPrimary,
			)
			Icon(
				imageVector = Icons.Filled.KeyboardArrowDown,
				contentDescription = stringResource(R.string.constructor_level_banner_action),
				tint = colorScheme.onPrimary,
				modifier = Modifier.size(18.dp),
			)
		}
		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false },
		) {
			levels.forEach { level ->
				DropdownMenuItem(
					text = {
						Text(
							text = level,
							fontWeight = if (level == selectedLevel) {
								FontWeight.SemiBold
							} else {
								FontWeight.Normal
							},
						)
					},
					onClick = {
						expanded = false
						if (level != selectedLevel) {
							onLevelChange(level)
						}
					},
				)
			}
		}
	}
}