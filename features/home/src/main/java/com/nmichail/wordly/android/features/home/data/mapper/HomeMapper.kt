package com.nmichail.wordly.android.features.home.data.mapper

import com.nmichail.wordly.android.features.home.data.dto.HomeResponse
import com.nmichail.wordly.android.features.home.domain.entity.HomePayload
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.home.domain.entity.TrainingId

internal fun HomeResponse.toDomain(): HomePayload =
	HomePayload(
		streakDays = streakDays,
		wordsToReview = wordsToReview,
		estimatedMinutes = estimatedMinutes,
		reviewStreakDays = reviewStreakDays,
		trainings = trainings.mapNotNull { it.toTrainingOrNull() },
		completedDayOffsets = completedDayOffsets,
	)

private fun String.toTrainingOrNull(): Training? {
	val id = TrainingId.all.find { it.equals(this, ignoreCase = true) } ?: return null
	return Training(id = id)
}
