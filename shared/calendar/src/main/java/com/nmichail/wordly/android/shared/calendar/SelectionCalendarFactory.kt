package com.nmichail.wordly.android.shared.calendar

import java.time.LocalDate
import java.time.YearMonth

object SelectionCalendarFactory {

	fun build(
		yearMonth: YearMonth,
		selectedEpochDay: Long,
		today: LocalDate = LocalDate.now(),
	): CalendarMonth {
		val selected = LocalDate.ofEpochDay(selectedEpochDay)
		val leadingEmpty = yearMonth.atDay(1).dayOfWeek.value - 1
		val days = ArrayList<CalendarDay?>(leadingEmpty + yearMonth.lengthOfMonth())
		repeat(leadingEmpty) { days.add(null) }
		for (day in 1..yearMonth.lengthOfMonth()) {
			val date = yearMonth.atDay(day)
			days.add(
				CalendarDay(
					dayOfMonth = day,
					status = when {
						date.isBefore(today) -> CalendarDayStatus.Inactive
						date == selected && date == today -> CalendarDayStatus.Today
						date == selected -> CalendarDayStatus.Selected
						date == today -> CalendarDayStatus.Today
						else -> CalendarDayStatus.Plain
					},
				),
			)
		}
		return CalendarMonth(
			monthTitle = MonthTitleFormatter.format(yearMonth),
			year = yearMonth.year,
			month = yearMonth.monthValue,
			days = days,
			selectedEpochDay = selectedEpochDay,
		)
	}
}
