package com.nmichail.wordly.android.features.home.domain.entity

data class MonthDay(
	val dayOfMonth: Int,
	val status: MonthDayStatus,
)

data class MonthDayStatus(
	val id: String,
)

object MonthDayStatusId {
	const val Completed = "completed"
	const val Missed = "missed"
	const val Today = "today"
	const val Inactive = "inactive"
}
