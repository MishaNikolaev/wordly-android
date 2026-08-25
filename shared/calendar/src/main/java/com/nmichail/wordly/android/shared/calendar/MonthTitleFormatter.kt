package com.nmichail.wordly.android.shared.calendar

import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

object MonthTitleFormatter {

	private val locale: Locale = Locale.forLanguageTag("ru")
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", locale)

	fun format(yearMonth: YearMonth): String =
		yearMonth.format(formatter).replaceFirstChar { it.titlecase(locale) }
}
