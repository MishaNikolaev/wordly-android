package com.nmichail.wordly.android.features.home.domain.usecase

import com.nmichail.wordly.android.features.home.domain.entity.Month
import com.nmichail.wordly.android.features.home.domain.entity.MonthDay
import com.nmichail.wordly.android.features.home.domain.entity.MonthDayStatus
import com.nmichail.wordly.android.features.home.domain.entity.MonthDayStatusId
import java.time.Clock
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.time.DayOfWeek as JavaDayOfWeek
import javax.inject.Inject

class GetMonthUseCase @Inject constructor(
	private val clock: Clock,
) {

	private val monthTitleFormatter: DateTimeFormatter =
		DateTimeFormatter.ofPattern("LLLL yyyy", Locale.forLanguageTag("ru"))

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
		val activeDays = days.count { it?.status?.id == MonthDayStatusId.Completed }
		val elapsedDays = days.count { day ->
			day != null && day.status.id != MonthDayStatusId.Inactive
		}

		return Month(
			title = yearMonth.format(monthTitleFormatter)
				.replaceFirstChar { it.titlecase(Locale.forLanguageTag("ru")) },
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
	): List<MonthDay?> {
		val firstDay = yearMonth.atDay(1)
		val leadingEmpty = firstDay.dayOfWeek.value - JavaDayOfWeek.MONDAY.value
		val days = ArrayList<MonthDay?>(leadingEmpty + yearMonth.lengthOfMonth())

		repeat(leadingEmpty) {
			days.add(null)
		}

		for (dayOfMonth in 1..yearMonth.lengthOfMonth()) {
			val date = yearMonth.atDay(dayOfMonth)
			val offsetFromToday = ChronoUnit.DAYS.between(today, date).toInt()
			days.add(
				MonthDay(
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
	): MonthDayStatus =
		when {
			date == today -> MonthDayStatus(id = MonthDayStatusId.Today)
			date.isAfter(today) -> MonthDayStatus(id = MonthDayStatusId.Inactive)
			offsetFromToday in completedOffsets -> MonthDayStatus(id = MonthDayStatusId.Completed)
			else -> MonthDayStatus(id = MonthDayStatusId.Missed)
		}
}
