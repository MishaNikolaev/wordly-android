package com.nmichail.wordly.android.features.home.domain.usecase

import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.repository.HomeRepository
import java.time.Clock
import java.time.YearMonth
import javax.inject.Inject

class GetHomeUseCase @Inject constructor(
	private val homeRepository: HomeRepository,
	private val getWeekDaysUseCase: GetWeekDaysUseCase,
	private val getMonthUseCase: GetMonthUseCase,
	private val clock: Clock,
) {

	suspend operator fun invoke(): Home {
		val payload = homeRepository.getHome()
		val completedOffsets = payload.completedDayOffsets.toSet()

		return Home(
			streakDays = payload.streakDays,
			wordsToReview = payload.wordsToReview,
			estimatedMinutes = payload.estimatedMinutes,
			reviewStreakDays = payload.reviewStreakDays,
			trainings = payload.trainings,
			weekDays = getWeekDaysUseCase(completedOffsets = completedOffsets),
			completedDayOffsets = payload.completedDayOffsets,
			month = getMonthUseCase(
				yearMonth = YearMonth.now(clock),
				completedOffsets = completedOffsets,
			),
		)
	}
}
