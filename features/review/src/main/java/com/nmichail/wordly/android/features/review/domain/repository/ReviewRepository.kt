package com.nmichail.wordly.android.features.review.domain.repository

import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord

interface ReviewRepository {

	suspend fun getSession(): List<ReviewWord>

	suspend fun submitAnswer(
		wordId: String,
		selectedOptionId: String,
		correct: Boolean,
	)
}