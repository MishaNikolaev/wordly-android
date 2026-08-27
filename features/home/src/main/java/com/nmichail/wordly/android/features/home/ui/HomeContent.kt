package com.nmichail.wordly.android.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.features.home.ui.component.DailyReviewCard
import com.nmichail.wordly.android.features.home.ui.component.HomeTopBar
import com.nmichail.wordly.android.features.home.ui.component.HomeTrainingFilter
import com.nmichail.wordly.android.features.home.ui.component.HomeTrainingTab
import com.nmichail.wordly.android.features.home.ui.component.TrainingFilterTabs
import com.nmichail.wordly.android.features.home.ui.component.TrainingFiltersBottomSheet
import com.nmichail.wordly.android.features.home.ui.component.WordsLearnedRecapCard
import com.nmichail.wordly.android.features.home.ui.component.homeGreeting
import com.nmichail.wordly.android.component.wui.components.snackbar.WuiSnackBarHost
import com.nmichail.wordly.android.component.wui.components.snackbar.showWuiInfoSnackBar
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.home.R
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeStore
import com.nmichail.wordly.android.component.wui.R as WuiR

@Composable
fun HomeContent(
	component: HomeComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val uiState = state) {
		HomeStore.State.Initial,
		HomeStore.State.Loading -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}

		HomeStore.State.Error -> {
			HomeError(
				onRetryClick = component::handleRetry,
				modifier = modifier.fillMaxSize(),
			)
		}

		is HomeStore.State.Content -> {
			HomeLoaded(
				state = uiState,
				component = component,
				modifier = modifier,
			)
		}
	}
}

@Composable
private fun HomeError(
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
			text = stringResource(R.string.home_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.home_error_description),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		WuiButton(
			text = stringResource(R.string.home_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
	}
}

@Composable
private fun HomeLoaded(
	state: HomeStore.State.Content,
	component: HomeComponent,
	modifier: Modifier = Modifier,
) {
	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current

	fun showStreakSnackBar(days: Int) {
		val message = if (days <= 0) {
			context.getString(R.string.home_streak_snackbar_empty)
		} else {
			context.resources.getQuantityString(R.plurals.home_streak_snackbar, days, days)
		}
		coroutineScope.showWuiInfoSnackBar(
			snackBarHostState = snackBarHostState,
			message = message,
		)
	}

	Box(modifier = modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.verticalScroll(rememberScrollState()),
		) {
			HomeTopBar(
				title = homeGreeting(firstName = state.firstName),
				streakDays = state.streakDays,
				onStreakClick = { showStreakSnackBar(state.streakDays) },
			)
			HomeLoadedBody(
				state = state,
				component = component,
				onStreakSnackBar = ::showStreakSnackBar,
				onUnavailableTraining = { messageRes ->
					coroutineScope.showWuiInfoSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(messageRes),
					)
				},
			)
		}
		WuiSnackBarHost(
			snackBarHostState = snackBarHostState,
			modifier = Modifier.align(Alignment.BottomCenter),
		)
	}
}

@Composable
private fun HomeLoadedBody(
	state: HomeStore.State.Content,
	component: HomeComponent,
	onStreakSnackBar: (Int) -> Unit,
	onUnavailableTraining: (Int) -> Unit,
) {
	var selectedTrainingTab by rememberSaveable { mutableStateOf<HomeTrainingTab?>(null) }
	var showTrainingFiltersSheet by rememberSaveable { mutableStateOf(false) }

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp)
			.padding(top = 16.dp, bottom = 24.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp),
	) {
		Column(modifier = Modifier.fillMaxWidth()) {
			DailyReviewCard(
				wordsToReview = state.wordsToReview,
				estimatedMinutes = state.estimatedMinutes,
				streakDays = state.reviewStreakDays,
				onStartClick = component::handleStartReview,
				onStreakClick = { onStreakSnackBar(state.reviewStreakDays) },
			)
			HomeQuote(modifier = Modifier.padding(top = 24.dp))
			WatchWithSubtitlesButton(
				onClick = component::handleOpenMovies,
				modifier = Modifier.padding(top = 16.dp),
			)
			TrainingFilterTabs(
				selectedTab = selectedTrainingTab,
				onGridClick = { showTrainingFiltersSheet = true },
				onTabSelected = { tab ->
					selectedTrainingTab = tab
					openTrainingTab(tab, component, onUnavailableTraining)
				},
				modifier = Modifier.padding(top = 16.dp),
			)
		}
		WordsLearnedRecapCard(
			onCtaClick = component::handleOpenRecap,
		)
	}

	if (showTrainingFiltersSheet) {
		TrainingFiltersBottomSheet(
			selectedTab = selectedTrainingTab,
			onDismiss = { showTrainingFiltersSheet = false },
			onFilterClick = { filter ->
				showTrainingFiltersSheet = false
				selectedTrainingTab = openTrainingFilter(filter, component, onUnavailableTraining)
					?: selectedTrainingTab
			},
		)
	}
}

private fun openTrainingTab(
	tab: HomeTrainingTab,
	component: HomeComponent,
	onUnavailableTraining: (Int) -> Unit,
) {
	when (tab) {
		HomeTrainingTab.Cards -> component.handleOpenCards()
		HomeTrainingTab.Constructor -> component.handleOpenConstructor()
		HomeTrainingTab.Listening -> onUnavailableTraining(R.string.home_training_listening_coming_soon)
		HomeTrainingTab.Books -> component.handleOpenBooks()
	}
}

private fun openTrainingFilter(
	filter: HomeTrainingFilter,
	component: HomeComponent,
	onUnavailableTraining: (Int) -> Unit,
): HomeTrainingTab? {
	when (filter) {
		HomeTrainingFilter.Cards -> component.handleOpenCards()
		HomeTrainingFilter.Constructor -> component.handleOpenConstructor()
		HomeTrainingFilter.Listening -> onUnavailableTraining(R.string.home_training_listening_coming_soon)
		HomeTrainingFilter.Songs -> onUnavailableTraining(R.string.home_training_songs_coming_soon)
		HomeTrainingFilter.Movies -> component.handleOpenMovies()
		HomeTrainingFilter.Books -> component.handleOpenBooks()
	}
	return filter.toTabOrNull()
}

@Composable
private fun HomeQuote(modifier: Modifier = Modifier) {
	Text(
		text = stringResource(R.string.home_quote),
		style = WuiTypography.homeQuote,
		color = MaterialTheme.colorScheme.onSurface,
		modifier = modifier.fillMaxWidth(),
	)
}

@Composable
private fun WatchWithSubtitlesButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	Box(
		modifier = modifier.fillMaxWidth(),
		contentAlignment = Alignment.Center,
	) {
		Row(
			modifier = Modifier
				.height(52.dp)
				.clip(RoundedCornerShape(26.dp))
				.background(colorScheme.onSurface)
				.clickable(role = Role.Button, onClick = onClick)
				.padding(horizontal = 28.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				painter = painterResource(WuiR.drawable.subtitles),
				contentDescription = null,
				tint = colorScheme.surface,
				modifier = Modifier.size(width = 15.dp, height = 13.dp),
			)
			Text(
				text = stringResource(R.string.home_watch_with_subtitles),
				style = WuiTypography.addWordExample.copy(fontWeight = FontWeight.Bold),
				color = colorScheme.surface,
			)
		}
	}
}
