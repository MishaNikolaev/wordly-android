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
		val home = homeRepository.getHome()
		val completedOffsets = home.completedDayOffsets.toSet()

		return home.copy(
			weekDays = getWeekDaysUseCase(completedOffsets = completedOffsets),
			month = getMonthUseCase(
				yearMonth = YearMonth.now(clock),
				completedOffsets = completedOffsets,
			),
		)
	}
}
