package com.nmichail.wordly.android.features.home.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.components.button.CustomButton
import com.nmichail.wordly.android.component.ui.components.calendar.CalendarDay
import com.nmichail.wordly.android.component.ui.components.calendar.CalendarDayStatusId
import com.nmichail.wordly.android.component.ui.components.calendar.CalendarDialog
import com.nmichail.wordly.android.features.home.ui.component.DailyReviewCard
import com.nmichail.wordly.android.features.home.ui.component.HomeTopBar
import com.nmichail.wordly.android.component.ui.components.text.SectionLabel
import com.nmichail.wordly.android.features.home.ui.component.TrainingListItem
import com.nmichail.wordly.android.features.home.ui.component.WeekDayIndicator
import com.nmichail.wordly.android.features.home.ui.component.WeekDayStatusId
import com.nmichail.wordly.android.features.home.ui.component.WeekProgressCard
import com.nmichail.wordly.android.features.home.ui.component.homeGreeting
import com.nmichail.wordly.android.component.ui.components.snackbar.SnackBarHost
import com.nmichail.wordly.android.component.ui.components.snackbar.showInfoSnackBar
import com.nmichail.wordly.android.features.home.R
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.home.presentation.calendar.MonthDayStatus
import com.nmichail.wordly.android.features.home.presentation.calendar.WeekDayStatus
import java.time.DayOfWeek

@Composable
fun HomeContent(
	component: HomeComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val currentState = state) {
		HomeComponent.State.Loading -> {
			Box(
				modifier = modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		HomeComponent.State.Error -> {
			HomeError(
				onRetryClick = component::handleRetry,
				modifier = modifier.fillMaxSize(),
			)
		}
		is HomeComponent.State.Content -> {
			HomeLoaded(
				state = currentState,
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
		CustomButton(
			text = stringResource(R.string.home_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
	}
}

@Composable
private fun HomeLoaded(
	state: HomeComponent.State.Content,
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
		coroutineScope.showInfoSnackBar(
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
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(top = 16.dp, bottom = 24.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp),
			) {
				WeekProgressCard(onMonthClick = component::handleOpenMonth) {
					state.weekDays.forEach { day ->
						WeekDayIndicator(
							label = stringResource(day.dayOfWeek.labelRes()),
							statusId = day.status.toUiId(),
							dayOfMonth = day.dayOfMonth,
							modifier = Modifier.weight(1f),
						)
					}
				}
				DailyReviewCard(
					wordsToReview = state.wordsToReview,
					estimatedMinutes = state.estimatedMinutes,
					streakDays = state.reviewStreakDays,
					onStartClick = component::handleStartReview,
					onStreakClick = { showStreakSnackBar(state.reviewStreakDays) },
				)
				TrainingsBlock(
					trainings = state.trainings,
					onTrainingClick = component::handleOpenTraining,
				)
			}
		}
		SnackBarHost(
			snackBarHostState = snackBarHostState,
			modifier = Modifier.align(Alignment.BottomCenter),
		)
	}

	if (state.isCalendarVisible) {
		HomeCalendarDialog(state = state, component = component)
	}
}

@Composable
private fun HomeCalendarDialog(
	state: HomeComponent.State.Content,
	component: HomeComponent,
) {
	CalendarDialog(
		monthTitle = state.monthTitle,
		days = state.monthDays.map { day ->
			day?.let {
				CalendarDay(
					dayOfMonth = it.dayOfMonth,
					statusId = it.status.toUiId(),
				)
			}
		},
		onDismiss = component::handleDismissMonth,
		onTodayClick = component::handleGoToCurrentMonth,
		onPreviousMonthClick = component::handlePreviousMonth,
		onNextMonthClick = component::handleNextMonth,
	)
}

@Composable
private fun TrainingsBlock(
	trainings: List<Training>,
	onTrainingClick: (Training) -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		SectionLabel(
			text = stringResource(R.string.home_trainings_section),
			modifier = Modifier.padding(start = 4.dp),
		)
		trainings.forEach { training ->
			TrainingListItem(
				title = training.title,
				subtitle = training.subtitle,
				icon = training.icon(),
				onClick = { onTrainingClick(training) },
			)
		}
	}
}

@StringRes
private fun DayOfWeek.labelRes(): Int =
	when (this) {
		DayOfWeek.MONDAY -> R.string.home_day_mon
		DayOfWeek.TUESDAY -> R.string.home_day_tue
		DayOfWeek.WEDNESDAY -> R.string.home_day_wed
		DayOfWeek.THURSDAY -> R.string.home_day_thu
		DayOfWeek.FRIDAY -> R.string.home_day_fri
		DayOfWeek.SATURDAY -> R.string.home_day_sat
		DayOfWeek.SUNDAY -> R.string.home_day_sun
	}

private fun WeekDayStatus.toUiId(): String =
	when (this) {
		WeekDayStatus.Completed -> WeekDayStatusId.Completed
		WeekDayStatus.Today -> WeekDayStatusId.Today
		WeekDayStatus.Missed -> WeekDayStatusId.Missed
		WeekDayStatus.Upcoming -> WeekDayStatusId.Upcoming
	}

private fun MonthDayStatus.toUiId(): String =
	when (this) {
		MonthDayStatus.Completed -> CalendarDayStatusId.Completed
		MonthDayStatus.Missed -> CalendarDayStatusId.Missed
		MonthDayStatus.Today -> CalendarDayStatusId.Today
		MonthDayStatus.Inactive -> CalendarDayStatusId.Inactive
	}

private fun Training.icon(): ImageVector =
	when (id) {
		"cards" -> Icons.Outlined.Style
		"constructor" -> Icons.Outlined.GridView
		"listening" -> Icons.Outlined.Headphones
		"songs" -> Icons.Outlined.MusicNote
		"movies" -> Icons.Outlined.Movie
		"books" -> Icons.Outlined.MenuBook
		else -> Icons.Outlined.Style
	}