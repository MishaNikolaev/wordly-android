package com.nmichail.wordly.android.features.home.data.dto

data class HomeResponse(
	val streakDays: Int,
	val wordsToReview: Int,
	val estimatedMinutes: Int,
	val reviewStreakDays: Int,
	val trainings: List<String>,
	val completedDayOffsets: List<Int>,
)
