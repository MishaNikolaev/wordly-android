package com.nmichail.wordly.android.features.home.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.CalendarDay
import com.nmichail.wordly.android.component.ui.components.CalendarDialog
import com.nmichail.wordly.android.component.ui.components.DailyReviewCard
import com.nmichail.wordly.android.component.ui.components.HomeTopBar
import com.nmichail.wordly.android.component.ui.components.SectionLabel
import com.nmichail.wordly.android.component.ui.components.TrainingTile
import com.nmichail.wordly.android.component.ui.components.WeekDayIndicator
import com.nmichail.wordly.android.component.ui.components.WeekProgressCard
import com.nmichail.wordly.android.features.home.R
import com.nmichail.wordly.android.features.home.domain.entity.DayOfWeek
import com.nmichail.wordly.android.features.home.domain.entity.DayOfWeekId
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.home.domain.entity.TrainingId
import com.nmichail.wordly.android.features.home.presentation.HomeComponent

@Composable
fun HomeContent(
	component: HomeComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.verticalScroll(rememberScrollState()),
	) {
		HomeTopBar(
			title = stringResource(R.string.home_screen_title),
			streakDays = state.streakDays,
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
						statusId = day.status.id,
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
			)
			TrainingsBlock(
				trainings = state.trainings,
				onTrainingClick = component::handleOpenTraining,
			)
		}
	}

	if (state.isCalendarVisible) {
		HomeCalendarDialog(state = state, component = component)
	}
}

@Composable
private fun HomeCalendarDialog(
	state: HomeComponent.State,
	component: HomeComponent,
) {
	CalendarDialog(
		monthTitle = state.monthTitle,
		days = state.monthDays.map { day ->
			day?.let {
				CalendarDay(
					dayOfMonth = it.dayOfMonth,
					statusId = it.status.id,
				)
			}
		},
		activeDaysCount = state.monthActiveDays,
		currentStreak = state.streakDays,
		completionPercent = state.monthCompletionPercent,
		onDismiss = component::handleDismissMonth,
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
			text = stringResource(ComponentR.string.home_trainings_section),
			modifier = Modifier.padding(start = 4.dp),
		)
		trainings.chunked(2).forEach { rowTrainings ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(12.dp),
			) {
				rowTrainings.forEach { training ->
					TrainingTile(
						title = stringResource(training.titleRes()),
						subtitle = stringResource(training.subtitleRes()),
						icon = training.icon(),
						onClick = { onTrainingClick(training) },
						modifier = Modifier.weight(1f),
					)
				}
				if (rowTrainings.size == 1) {
					Spacer(modifier = Modifier.weight(1f))
				}
			}
		}
	}
}

@StringRes
private fun DayOfWeek.labelRes(): Int =
	when (id) {
		DayOfWeekId.Mon -> ComponentR.string.home_day_mon
		DayOfWeekId.Tue -> ComponentR.string.home_day_tue
		DayOfWeekId.Wed -> ComponentR.string.home_day_wed
		DayOfWeekId.Thu -> ComponentR.string.home_day_thu
		DayOfWeekId.Fri -> ComponentR.string.home_day_fri
		DayOfWeekId.Sat -> ComponentR.string.home_day_sat
		else -> ComponentR.string.home_day_sun
	}

@StringRes
private fun Training.titleRes(): Int =
	when (id) {
		TrainingId.Cards -> ComponentR.string.home_training_cards_title
		TrainingId.Constructor -> ComponentR.string.home_training_constructor_title
		TrainingId.Listening -> ComponentR.string.home_training_listening_title
		TrainingId.Songs -> ComponentR.string.home_training_songs_title
		TrainingId.Movies -> ComponentR.string.home_training_movies_title
		TrainingId.Books -> ComponentR.string.home_training_books_title
		else -> ComponentR.string.home_training_cards_title
	}

@StringRes
private fun Training.subtitleRes(): Int =
	when (id) {
		TrainingId.Cards -> ComponentR.string.home_training_cards_subtitle
		TrainingId.Constructor -> ComponentR.string.home_training_constructor_subtitle
		TrainingId.Listening -> ComponentR.string.home_training_listening_subtitle
		TrainingId.Songs -> ComponentR.string.home_training_songs_subtitle
		TrainingId.Movies -> ComponentR.string.home_training_movies_subtitle
		TrainingId.Books -> ComponentR.string.home_training_books_subtitle
		else -> ComponentR.string.home_training_cards_subtitle
	}

private fun Training.icon(): ImageVector =
	when (id) {
		TrainingId.Cards -> Icons.Outlined.Style
		TrainingId.Constructor -> Icons.Outlined.GridView
		TrainingId.Listening -> Icons.Outlined.Headphones
		TrainingId.Songs -> Icons.Outlined.MusicNote
		TrainingId.Movies -> Icons.Outlined.Movie
		TrainingId.Books -> Icons.Outlined.MenuBook
		else -> Icons.Outlined.Style
	}
