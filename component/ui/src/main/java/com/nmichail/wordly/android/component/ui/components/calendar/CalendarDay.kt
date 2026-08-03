package com.nmichail.wordly.android.component.ui.components.calendar

data class CalendarDay(
	val dayOfMonth: Int,
	val statusId: String,
)

object CalendarDayStatusId {
	const val Completed = "completed"
	const val Missed = "missed"
	const val Today = "today"
	const val Inactive = "inactive"
	const val Selected = "selected"
}