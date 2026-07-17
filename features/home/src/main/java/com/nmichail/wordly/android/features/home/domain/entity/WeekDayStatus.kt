package com.nmichail.wordly.android.features.home.domain.entity

data class WeekDayStatus(
	val id: String,
)

object WeekDayStatusId {
	const val Completed = "completed"
	const val Today = "today"
	const val Missed = "missed"
	const val Upcoming = "upcoming"
}
