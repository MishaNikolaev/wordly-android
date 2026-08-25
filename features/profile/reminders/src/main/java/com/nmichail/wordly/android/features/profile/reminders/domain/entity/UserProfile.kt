package com.nmichail.wordly.android.features.profile.reminders.domain.entity

data class UserProfile(
	val id: String,
	val email: String,
	val firstName: String,
	val lastName: String,
	val englishLevel: String,
	val dailyGoal: DailyGoal,
	val notificationTimes: List<NotificationTimeSlot>,
) {

	val fullName: String
		get() = listOf(firstName, lastName)
			.filter { it.isNotBlank() }
			.joinToString(separator = " ")
}
