package com.nmichail.wordly.android.features.review.domain.usecase

import com.nmichail.wordly.android.features.review.domain.repository.ReviewRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class SubmitReviewAnswerUseCaseTest {

	private val reviewRepository: ReviewRepository = mock()
	private val submitReviewAnswerUseCase = SubmitReviewAnswerUseCase(reviewRepository)

	@ParameterizedTest
	@ValueSource(booleans = [true, false])
	fun `invoke EXPECT repository submit answer`(
		correct: Boolean,
	) = runTest {
		submitReviewAnswerUseCase("recall", "recall-2", correct)

		verify(reviewRepository).submitAnswer(
			wordId = "recall",
			selectedOptionId = "recall-2",
			correct = correct,
		)
	}
}