package com.nmichail.wordly.android.features.home.domain.usecase

import com.nmichail.wordly.android.features.home.domain.entity.DayOfWeek
import com.nmichail.wordly.android.features.home.domain.entity.DayOfWeekId
import com.nmichail.wordly.android.features.home.domain.entity.WeekDay
import com.nmichail.wordly.android.features.home.domain.entity.WeekDayStatus
import com.nmichail.wordly.android.features.home.domain.entity.WeekDayStatusId
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek as JavaDayOfWeek
import javax.inject.Inject

private const val DAYS_IN_WEEK = 7

class GetWeekDaysUseCase @Inject constructor(
	private val clock: Clock,
) {

	operator fun invoke(completedOffsets: Set<Int>): List<WeekDay> {
		val today = LocalDate.now(clock)
		val monday = today.with(TemporalAdjusters.previousOrSame(JavaDayOfWeek.MONDAY))

		return (0 until DAYS_IN_WEEK).map { dayOffset ->
			val date = monday.plusDays(dayOffset.toLong())
			val offsetFromToday = ChronoUnit.DAYS.between(today, date).toInt()
			WeekDay(
				dayOfWeek = date.dayOfWeek.toDomain(),
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
			date == today -> WeekDayStatus(id = WeekDayStatusId.Today)
			date.isAfter(today) -> WeekDayStatus(id = WeekDayStatusId.Upcoming)
			offsetFromToday in completedOffsets -> WeekDayStatus(id = WeekDayStatusId.Completed)
			else -> WeekDayStatus(id = WeekDayStatusId.Missed)
		}

	private fun JavaDayOfWeek.toDomain(): DayOfWeek =
		when (this) {
			JavaDayOfWeek.MONDAY -> DayOfWeek(id = DayOfWeekId.Mon)
			JavaDayOfWeek.TUESDAY -> DayOfWeek(id = DayOfWeekId.Tue)
			JavaDayOfWeek.WEDNESDAY -> DayOfWeek(id = DayOfWeekId.Wed)
			JavaDayOfWeek.THURSDAY -> DayOfWeek(id = DayOfWeekId.Thu)
			JavaDayOfWeek.FRIDAY -> DayOfWeek(id = DayOfWeekId.Fri)
			JavaDayOfWeek.SATURDAY -> DayOfWeek(id = DayOfWeekId.Sat)
			JavaDayOfWeek.SUNDAY -> DayOfWeek(id = DayOfWeekId.Sun)
		}
}
