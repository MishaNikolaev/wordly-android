package com.nmichail.wordly.android.features.home.data.dto

import com.nmichail.wordly.android.features.news.data.dto.NewsResponse

data class HomeResponse(
	val firstName: String,
	val streakDays: Int,
	val wordsToReview: Int,
	val estimatedMinutes: Int,
	val reviewStreakDays: Int,
	val trainings: List<TrainingResponse>,
	val completedDayOffsets: List<Int>,
	val news: List<NewsResponse>,
)
