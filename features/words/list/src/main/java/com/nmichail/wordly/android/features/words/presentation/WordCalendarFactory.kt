package com.nmichail.wordly.android.features.words.presentation

import com.nmichail.wordly.android.component.wui.components.calendar.WuiCalendarDay
import com.nmichail.wordly.android.component.wui.components.calendar.WuiCalendarDayStatusId
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
		val days = ArrayList<WuiCalendarDay?>(leadingEmpty + yearMonth.lengthOfMonth())
		repeat(leadingEmpty) { days.add(null) }
		for (day in 1..yearMonth.lengthOfMonth()) {
			val date = yearMonth.atDay(day)
			days.add(
				WuiCalendarDay(
					dayOfMonth = day,
					statusId = when {
						date.isBefore(today) -> WuiCalendarDayStatusId.Inactive
						date == selected && date == today -> WuiCalendarDayStatusId.Today
						date == selected -> WuiCalendarDayStatusId.Selected
						date == today -> WuiCalendarDayStatusId.Today
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