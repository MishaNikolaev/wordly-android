package com.nmichail.wordly.android.features.profile.editor.domain.entity

data class DailyGoal(
	val wordsPerDay: Int,
)

object DailyGoals {

	val options: List<DailyGoal> = listOf(
		DailyGoal(wordsPerDay = 5),
		DailyGoal(wordsPerDay = 10),
		DailyGoal(wordsPerDay = 15),
		DailyGoal(wordsPerDay = 20),
	)
}