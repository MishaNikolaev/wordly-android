package com.nmichail.wordly.android.features.review.data.repository

import com.nmichail.wordly.android.features.review.data.api.ReviewApi
import com.nmichail.wordly.android.features.review.data.dto.ReviewAnswerRequest
import com.nmichail.wordly.android.features.review.data.mapper.toEntity
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord
import com.nmichail.wordly.android.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
	private val reviewApi: ReviewApi,
) : ReviewRepository {

	override suspend fun getSession(): List<ReviewWord> =
		reviewApi.getSession().words.map { it.toEntity() }

	override suspend fun submitAnswer(
		wordId: String,
		selectedOptionId: String,
		correct: Boolean,
	) {
		reviewApi.submitAnswer(
			ReviewAnswerRequest(
				wordId = wordId,
				selectedOptionId = selectedOptionId,
				correct = correct,
			),
		)
	}
}