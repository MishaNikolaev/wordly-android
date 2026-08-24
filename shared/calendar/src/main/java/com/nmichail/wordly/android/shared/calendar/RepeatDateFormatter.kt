package com.nmichail.wordly.android.shared.calendar

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object RepeatDateFormatter {

	private val monthDayFormatter =
		DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"))

	fun label(epochDay: Long?, today: LocalDate = LocalDate.now()): String {
		val date = epochDay?.let(LocalDate::ofEpochDay) ?: today
		return if (date == today) {
			"сегодня"
		} else {
			date.format(monthDayFormatter)
		}
	}
}
