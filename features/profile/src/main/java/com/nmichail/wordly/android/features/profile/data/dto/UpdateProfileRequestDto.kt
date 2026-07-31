package com.nmichail.wordly.android.features.profile.data.dto

data class UpdateProfileRequestDto(
	val firstName: String?,
	val lastName: String?,
	val englishLevel: String?,
	val dailyGoalWords: Int?,
	val notificationTimes: List<String>?,
)
