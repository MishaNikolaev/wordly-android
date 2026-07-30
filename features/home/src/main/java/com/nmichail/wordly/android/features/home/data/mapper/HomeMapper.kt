package com.nmichail.wordly.android.features.home.data.mapper

import com.nmichail.wordly.android.features.home.data.dto.HomeResponse
import com.nmichail.wordly.android.features.home.data.dto.TrainingResponse
import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.entity.Training

internal fun HomeResponse.toEntity(): Home =
	Home(
		firstName = firstName,
		streakDays = streakDays,
		wordsToReview = wordsToReview,
		estimatedMinutes = estimatedMinutes,
		reviewStreakDays = reviewStreakDays,
		trainings = trainings.map { it.toEntity() },
		completedDayOffsets = completedDayOffsets,
	)

private fun TrainingResponse.toEntity(): Training =
	Training(
		id = id,
		title = title,
		subtitle = subtitle,
	)
