package com.nmichail.wordly.android.features.home.domain.entity

data class HomePayload(
	val streakDays: Int,
	val wordsToReview: Int,
	val estimatedMinutes: Int,
	val reviewStreakDays: Int,
	val trainings: List<Training>,
	val completedDayOffsets: List<Int>,
)
