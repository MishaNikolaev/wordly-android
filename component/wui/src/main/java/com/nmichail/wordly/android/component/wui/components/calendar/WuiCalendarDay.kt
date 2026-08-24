package com.nmichail.wordly.android.component.wui.components.calendar

data class WuiCalendarDay(
	val dayOfMonth: Int,
	val statusId: String,
)

object WuiCalendarDayStatusId {
	const val Completed = "completed"
	const val Missed = "missed"
	const val Today = "today"
	const val Inactive = "inactive"
	const val Selected = "selected"
	const val Plain = "plain"
}