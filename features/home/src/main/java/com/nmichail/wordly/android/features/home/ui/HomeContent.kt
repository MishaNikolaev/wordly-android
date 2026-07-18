package com.nmichail.wordly.android.features.home.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.nmichail.wordly.android.component.ui.components.CalendarDayStatusId
import com.nmichail.wordly.android.component.ui.components.CalendarDialog
import com.nmichail.wordly.android.component.ui.components.DailyReviewCard
import com.nmichail.wordly.android.component.ui.components.HomeTopBar
import com.nmichail.wordly.android.component.ui.components.NewsCard
import com.nmichail.wordly.android.component.ui.components.SectionLabel
import com.nmichail.wordly.android.component.ui.components.TrainingListItem
import com.nmichail.wordly.android.component.ui.components.WeekDayIndicator
import com.nmichail.wordly.android.component.ui.components.WeekDayStatusId
import com.nmichail.wordly.android.component.ui.components.WeekProgressCard
import com.nmichail.wordly.android.component.ui.components.homeGreeting
import com.nmichail.wordly.android.features.home.domain.entity.MonthDayStatus
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.home.domain.entity.WeekDayStatus
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.news.domain.entity.News
import java.time.DayOfWeek

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
			title = homeGreeting(firstName = state.firstName),
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
			)
			TrainingsBlock(
				trainings = state.trainings,
				onTrainingClick = component::handleOpenTraining,
			)
			NewsBlock(
				news = state.news,
				onNewsClick = component::handleOpenNews,
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
			text = stringResource(ComponentR.string.home_trainings_section),
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

@Composable
private fun NewsBlock(
	news: List<News>,
	onNewsClick: (News) -> Unit,
) {
	if (news.isEmpty()) {
		return
	}
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		SectionLabel(
			text = stringResource(ComponentR.string.home_news_section),
			modifier = Modifier.padding(start = 4.dp),
		)
		news.forEach { item ->
			NewsCard(
				title = item.title,
				subtitle = item.subtitle,
				publishedAt = item.publishedAt,
				onClick = { onNewsClick(item) },
			)
		}
	}
}

@StringRes
private fun DayOfWeek.labelRes(): Int =
	when (this) {
		DayOfWeek.MONDAY -> ComponentR.string.home_day_mon
		DayOfWeek.TUESDAY -> ComponentR.string.home_day_tue
		DayOfWeek.WEDNESDAY -> ComponentR.string.home_day_wed
		DayOfWeek.THURSDAY -> ComponentR.string.home_day_thu
		DayOfWeek.FRIDAY -> ComponentR.string.home_day_fri
		DayOfWeek.SATURDAY -> ComponentR.string.home_day_sat
		DayOfWeek.SUNDAY -> ComponentR.string.home_day_sun
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
