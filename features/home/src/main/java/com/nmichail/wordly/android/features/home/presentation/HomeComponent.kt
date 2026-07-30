package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.home.presentation.calendar.MonthDay
import com.nmichail.wordly.android.features.home.presentation.calendar.WeekDay

interface HomeComponent {

	val model: Value<State>

	fun handleRetry()

	fun handleOpenMonth()

	fun handleDismissMonth()

	fun handlePreviousMonth()

	fun handleNextMonth()

	fun handleGoToCurrentMonth()

	fun handleStartReview()

	fun handleOpenTraining(training: Training)

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val firstName: String,
			val streakDays: Int,
			val weekDays: List<WeekDay>,
			val wordsToReview: Int,
			val estimatedMinutes: Int,
			val reviewStreakDays: Int,
			val trainings: List<Training>,
			val monthTitle: String,
			val monthDays: List<MonthDay?>,
			val monthActiveDays: Int,
			val monthCompletionPercent: Int,
			val isCalendarVisible: Boolean,
		) : State
	}

	sealed interface Label {

		data object StartReview : Label

		data class OpenTraining(val training: Training) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			homeRouter: HomeRouter,
		): HomeComponent
	}
}
