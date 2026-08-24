package com.nmichail.wordly.android.shared.calendar

data class Month(
	val title: String,
	val days: List<CalendarDay?>,
	val activeDays: Int,
	val completionPercent: Int,
)
