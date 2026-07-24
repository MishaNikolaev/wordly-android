package com.nmichail.wordly.android.features.review.domain.usecase

import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord
import com.nmichail.wordly.android.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class GetReviewSessionUseCase @Inject constructor(
	private val reviewRepository: ReviewRepository,
) {
	suspend operator fun invoke(): List<ReviewWord> =
		reviewRepository.getSession()
}