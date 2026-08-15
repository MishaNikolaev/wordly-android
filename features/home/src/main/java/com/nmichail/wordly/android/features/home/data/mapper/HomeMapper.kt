package com.nmichail.wordly.android.features.home.data.mapper

import com.nmichail.wordly.android.features.home.data.dto.HomeResponse
import com.nmichail.wordly.android.features.home.data.dto.TrainingResponse
import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.home.domain.entity.TrainingType

internal fun HomeResponse.toEntity(): Home =
	Home(
		firstName = firstName,
		streakDays = streakDays,
		wordsToReview = wordsToReview,
		estimatedMinutes = estimatedMinutes,
		reviewStreakDays = reviewStreakDays,
		trainings = trainings.mapNotNull { it.toEntity() },
		completedDayOffsets = completedDayOffsets,
	)

private fun TrainingResponse.toEntity(): Training? {
	val type = when (id) {
		"cards" -> TrainingType.Cards
		"constructor" -> TrainingType.Constructor
		"books" -> TrainingType.Books
		else -> return null
	}
	return Training(
		type = type,
		title = title,
		subtitle = subtitle,
	)
}
