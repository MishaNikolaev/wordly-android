package com.nmichail.wordly.android.shared.calendar

import java.time.DayOfWeek

data class WeekDay(
	val dayOfWeek: DayOfWeek,
	val dayOfMonth: Int,
	val status: WeekDayStatus,
)
