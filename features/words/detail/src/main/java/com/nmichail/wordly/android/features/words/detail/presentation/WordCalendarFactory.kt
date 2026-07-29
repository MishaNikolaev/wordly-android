package com.nmichail.wordly.android.features.words.detail.presentation

import com.nmichail.wordly.android.component.ui.components.CalendarDay
import com.nmichail.wordly.android.component.ui.components.CalendarDayStatusId
import java.time.LocalDate
import java.time.YearMonth
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

object WordCalendarFactory {

	fun build(
		yearMonth: YearMonth,
		selectedEpochDay: Long,
		today: LocalDate = LocalDate.now(),
	): CalendarState {
		val selected = LocalDate.ofEpochDay(selectedEpochDay)
		val leadingEmpty = yearMonth.atDay(1).dayOfWeek.value - 1
		val days = ArrayList<CalendarDay?>(leadingEmpty + yearMonth.lengthOfMonth())
		repeat(leadingEmpty) { days.add(null) }
		for (day in 1..yearMonth.lengthOfMonth()) {
			val date = yearMonth.atDay(day)
			days.add(
				CalendarDay(
					dayOfMonth = day,
					statusId = when {
						date.isBefore(today) -> CalendarDayStatusId.Inactive
						date == selected && date == today -> CalendarDayStatusId.Today
						date == selected -> CalendarDayStatusId.Selected
						date == today -> CalendarDayStatusId.Today
						else -> "plain"
					},
				),
			)
		}
		val monthTitle = yearMonth.format(
			DateTimeFormatter.ofPattern("LLLL yyyy", Locale.forLanguageTag("ru")),
		).replaceFirstChar { it.titlecase(Locale.forLanguageTag("ru")) }
		return CalendarState(
			monthTitle = monthTitle,
			year = yearMonth.year,
			month = yearMonth.monthValue,
			days = days,
			selectedEpochDay = selectedEpochDay,
		)
	}
}