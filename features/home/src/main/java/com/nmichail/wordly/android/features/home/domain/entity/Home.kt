package com.nmichail.wordly.android.features.home.domain.entity

data class Home(
	val firstName: String,
	val streakDays: Int,
	val wordsToReview: Int,
	val estimatedMinutes: Int,
	val reviewStreakDays: Int,
	val trainings: List<Training>,
)
