package com.nmichail.wordly.android.features.profile.editor.domain.entity

data class UpdateProfileParams(
	val firstName: String,
	val lastName: String,
	val englishLevel: String,
	val dailyGoalWords: Int?,
	val notificationTimes: List<String>?,
)
