package com.nmichail.wordly.android.features.review.domain.usecase

import com.nmichail.wordly.android.features.review.domain.repository.ReviewRepository
import javax.inject.Inject

class SubmitReviewAnswerUseCase @Inject constructor(
	reviewRepository: ReviewRepository,
) : suspend (String, String, Boolean) -> Unit by reviewRepository::submitAnswer
