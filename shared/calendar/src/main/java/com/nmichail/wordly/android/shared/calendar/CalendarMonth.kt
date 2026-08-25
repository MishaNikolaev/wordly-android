package com.nmichail.wordly.android.shared.calendar

data class CalendarMonth(
	val monthTitle: String,
	val year: Int,
	val month: Int,
	val days: List<CalendarDay?>,
	val selectedEpochDay: Long,
)
