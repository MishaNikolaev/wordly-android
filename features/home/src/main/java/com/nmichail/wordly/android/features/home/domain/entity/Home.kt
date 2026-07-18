package com.nmichail.wordly.android.features.home.domain.entity

import com.nmichail.wordly.android.features.news.domain.entity.News

data class Home(
	val firstName: String,
	val streakDays: Int,
	val wordsToReview: Int,
	val estimatedMinutes: Int,
	val reviewStreakDays: Int,
	val trainings: List<Training>,
	val news: List<News>,
	val completedDayOffsets: List<Int>,
	val weekDays: List<WeekDay> = emptyList(),
	val month: Month = Month(
		title = "",
		days = emptyList(),
		activeDays = 0,
		completionPercent = 0,
	),
)
