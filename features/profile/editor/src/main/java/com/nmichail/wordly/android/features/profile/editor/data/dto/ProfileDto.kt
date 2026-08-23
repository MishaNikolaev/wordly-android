package com.nmichail.wordly.android.features.profile.editor.data.dto

data class ProfileDto(
	val id: String,
	val email: String,
	val firstName: String,
	val lastName: String,
	val englishLevel: String,
	val dailyGoalWords: Int?,
	val notificationTimes: List<String>?,
)
