package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.shared.calendar.CalendarDay
import com.nmichail.wordly.android.shared.calendar.WeekDay
import java.time.YearMonth

interface HomeStore :
	Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Loading : State

		data class Content(
			val firstName: String,
			val streakDays: Int,
			val weekDays: List<WeekDay>,
			val wordsToReview: Int,
			val estimatedMinutes: Int,
			val reviewStreakDays: Int,
			val trainings: List<Training>,
			val completedDayOffsets: Set<Int>,
			val displayedMonth: YearMonth,
			val monthTitle: String,
			val monthDays: List<CalendarDay?>,
			val monthActiveDays: Int,
			val monthCompletionPercent: Int,
			val calendarVisible: Boolean,
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object StartReview : Label

		data object OpenCards : Label

		data object OpenConstructor : Label

		data object OpenBooks : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data object OpenMonth : Intent

		data object DismissMonth : Intent

		data object PreviousMonth : Intent

		data object NextMonth : Intent

		data object GoToCurrentMonth : Intent

		data object StartReview : Intent

		data class OpenTraining(val training: Training) : Intent
	}
}