package com.nmichail.wordly.android.features.home.domain.entity

data class DayOfWeek(
	val id: String,
)

object DayOfWeekId {
	const val Mon = "mon"
	const val Tue = "tue"
	const val Wed = "wed"
	const val Thu = "thu"
	const val Fri = "fri"
	const val Sat = "sat"
	const val Sun = "sun"
}
