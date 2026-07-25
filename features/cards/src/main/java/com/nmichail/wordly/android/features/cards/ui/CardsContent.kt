package com.nmichail.wordly.android.features.cards.ui

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
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.CatalogCard
import com.nmichail.wordly.android.component.ui.components.SearchField
import com.nmichail.wordly.android.component.ui.components.SectionLabel
import com.nmichail.wordly.android.component.ui.components.TextLink
import com.nmichail.wordly.android.features.cards.R
import com.nmichail.wordly.android.features.cards.domain.entity.CardsLevelBanner
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent

@Composable
fun CardsContent(
	component: CardsComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val currentState = state) {
		CardsComponent.State.Loading -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		CardsComponent.State.Error -> {
			CardsError(
				onRetryClick = component::handleRetry,
				onBackClick = component::handleBack,
				modifier = modifier.fillMaxSize(),
			)
		}
		is CardsComponent.State.Content -> {
			CardsLoaded(
				state = currentState,
				component = component,
				modifier = modifier,
			)
		}
	}
}

@Composable
private fun CardsError(
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
			text = stringResource(R.string.cards_error_title),
			style = MaterialTheme.typography.titleLarge,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.cards_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		Button(
			text = stringResource(R.string.cards_retry),
			onClick = onRetryClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
		)
		TextLink(
			text = stringResource(R.string.cards_back),
			onClick = onBackClick,
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}

@Composable
private fun CardsLoaded(
	state: CardsComponent.State.Content,
	component: CardsComponent,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding(),
	) {
		CardsTopBar(
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
					CardsLevelBannerContent(
						banner = banner,
						onLevelChange = component::handleLevelChange,
					)
				}
			}
			item(key = "search") {
				SearchField(
					value = state.searchQuery,
					onValueChange = component::handleSearchQueryChange,
					placeholder = state.searchPlaceholder,
				)
			}
			state.sections.forEach { section ->
				item(key = "section_${section.title}") {
					SectionLabel(
						text = section.title,
						modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
					)
				}
				items(
					items = section.items,
					key = { it.id },
				) { item ->
					CatalogCard(
						title = item.title,
						subtitle = item.subtitle,
						badge = item.badge,
						imageUrl = item.imageUrl,
						onClick = { component.handleCardClick(item.id) },
					)
				}
			}
		}
	}
}

@Composable
private fun CardsTopBar(
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
				contentDescription = stringResource(R.string.cards_back),
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
private fun CardsLevelBannerContent(
	banner: CardsLevelBanner,
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
		CardsLevelSelector(
			selectedLevel = banner.levelLabel,
			levels = banner.levels.ifEmpty { listOf(banner.levelLabel) },
			onLevelChange = onLevelChange,
		)
	}
}

@Composable
private fun CardsLevelSelector(
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
				contentDescription = stringResource(R.string.cards_level_banner_action),
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
