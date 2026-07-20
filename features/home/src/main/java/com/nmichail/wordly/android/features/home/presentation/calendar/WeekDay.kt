package com.nmichail.wordly.android.features.home.presentation.calendar

import java.time.DayOfWeek

data class WeekDay(
	val dayOfWeek: DayOfWeek,
	val dayOfMonth: Int,
	val status: WeekDayStatus,
)