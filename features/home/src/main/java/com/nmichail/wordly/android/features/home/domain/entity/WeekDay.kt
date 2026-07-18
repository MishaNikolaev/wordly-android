package com.nmichail.wordly.android.features.home.domain.entity

import java.time.DayOfWeek

data class WeekDay(
	val dayOfWeek: DayOfWeek,
	val dayOfMonth: Int,
	val status: WeekDayStatus,
)
