package com.nmichail.wordly.android.features.home.presentation.calendar

import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

private const val DAYS_IN_WEEK = 7

class WeekDaysFactory @Inject constructor(
	private val clock: Clock,
) {

	operator fun invoke(completedOffsets: Set<Int>): List<WeekDay> {
		val today = LocalDate.now(clock)
		val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

		return (0 until DAYS_IN_WEEK).map { dayOffset ->
			val date = monday.plusDays(dayOffset.toLong())
			val offsetFromToday = ChronoUnit.DAYS.between(today, date).toInt()
			WeekDay(
				dayOfWeek = date.dayOfWeek,
				dayOfMonth = date.dayOfMonth,
				status = resolveWeekStatus(
					date = date,
					today = today,
					offsetFromToday = offsetFromToday,
					completedOffsets = completedOffsets,
				),
			)
		}
	}

	private fun resolveWeekStatus(
		date: LocalDate,
		today: LocalDate,
		offsetFromToday: Int,
		completedOffsets: Set<Int>,
	): WeekDayStatus =
		when {
			date == today -> WeekDayStatus.Today
			date.isAfter(today) -> WeekDayStatus.Upcoming
			offsetFromToday in completedOffsets -> WeekDayStatus.Completed
			else -> WeekDayStatus.Missed
		}
}