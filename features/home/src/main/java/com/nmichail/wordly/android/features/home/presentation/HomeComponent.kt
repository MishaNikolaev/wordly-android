package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.home.domain.entity.MonthDay
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.home.domain.entity.WeekDay
import com.nmichail.wordly.android.features.news.domain.entity.News

interface HomeComponent {

	val model: Value<State>

	fun handleOpenMonth()

	fun handleDismissMonth()

	fun handlePreviousMonth()

	fun handleNextMonth()

	fun handleGoToCurrentMonth()

	fun handleStartReview()

	fun handleOpenTraining(training: Training)

	fun handleOpenNews(news: News)

	data class State(
		val firstName: String,
		val streakDays: Int,
		val weekDays: List<WeekDay>,
		val wordsToReview: Int,
		val estimatedMinutes: Int,
		val reviewStreakDays: Int,
		val trainings: List<Training>,
		val news: List<News>,
		val monthTitle: String,
		val monthDays: List<MonthDay?>,
		val monthActiveDays: Int,
		val monthCompletionPercent: Int,
		val isCalendarVisible: Boolean,
	)

	sealed interface Label {

		data object StartReview : Label

		data class OpenTraining(val training: Training) : Label

		data class OpenNews(val news: News) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			homeRouter: HomeRouter,
		): HomeComponent
	}
}
