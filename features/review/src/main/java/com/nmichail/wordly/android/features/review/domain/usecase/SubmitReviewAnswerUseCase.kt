package com.nmichail.wordly.android.features.review.domain.usecase

import com.nmichail.wordly.android.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class SubmitReviewAnswerUseCase @Inject constructor(
	private val reviewRepository: ReviewRepository,
) {
	suspend operator fun invoke(
		wordId: String,
		selectedOptionId: String,
		correct: Boolean,
	) {
		reviewRepository.submitAnswer(
			wordId = wordId,
			selectedOptionId = selectedOptionId,
			correct = correct,
		)
	}
}