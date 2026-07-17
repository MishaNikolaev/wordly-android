package com.nmichail.wordly.android.features.home.domain.entity

data class WeekDay(
	val dayOfWeek: DayOfWeek,
	val dayOfMonth: Int,
	val status: WeekDayStatus,
)
