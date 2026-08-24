package com.nmichail.wordly.android.shared.calendar

import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class MonthFactory @Inject constructor(
	private val clock: Clock,
) {

	operator fun invoke(
		yearMonth: YearMonth,
		completedOffsets: Set<Int>,
	): Month {
		val today = LocalDate.now(clock)
		val days = buildMonthDays(
			yearMonth = yearMonth,
			today = today,
			completedOffsets = completedOffsets,
		)
		val activeDays = days.count { it?.status == CalendarDayStatus.Completed }
		val elapsedDays = days.count { day ->
			day != null && day.status != CalendarDayStatus.Inactive
		}

		return Month(
			title = MonthTitleFormatter.format(yearMonth),
			days = days,
			activeDays = activeDays,
			completionPercent = if (elapsedDays == 0) {
				0
			} else {
				(activeDays * 100) / elapsedDays
			},
		)
	}

	private fun buildMonthDays(
		yearMonth: YearMonth,
		today: LocalDate,
		completedOffsets: Set<Int>,
	): List<CalendarDay?> {
		val firstDay = yearMonth.atDay(1)
		val leadingEmpty = firstDay.dayOfWeek.value - DayOfWeek.MONDAY.value
		val days = ArrayList<CalendarDay?>(leadingEmpty + yearMonth.lengthOfMonth())

		repeat(leadingEmpty) {
			days.add(null)
		}

		for (dayOfMonth in 1..yearMonth.lengthOfMonth()) {
			val date = yearMonth.atDay(dayOfMonth)
			val offsetFromToday = ChronoUnit.DAYS.between(today, date).toInt()
			days.add(
				CalendarDay(
					dayOfMonth = dayOfMonth,
					status = resolveMonthStatus(
						date = date,
						today = today,
						offsetFromToday = offsetFromToday,
						completedOffsets = completedOffsets,
					),
				),
			)
		}

		return days
	}

	private fun resolveMonthStatus(
		date: LocalDate,
		today: LocalDate,
		offsetFromToday: Int,
		completedOffsets: Set<Int>,
	): CalendarDayStatus =
		when {
			date == today -> CalendarDayStatus.Today
			date.isAfter(today) -> CalendarDayStatus.Inactive
			offsetFromToday in completedOffsets -> CalendarDayStatus.Completed
			else -> CalendarDayStatus.Missed
		}
}
