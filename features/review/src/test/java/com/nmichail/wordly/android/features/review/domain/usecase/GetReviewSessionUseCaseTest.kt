package com.nmichail.wordly.android.features.review.domain.usecase

import com.nmichail.wordly.android.features.review.domain.entity.ReviewOption
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord
import com.nmichail.wordly.android.features.review.domain.repository.ReviewRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetReviewSessionUseCaseTest {

	private val reviewRepository: ReviewRepository = mock()
	private val getReviewSessionUseCase = GetReviewSessionUseCase(reviewRepository)

	private val words = listOf(
		ReviewWord(
			id = "recall",
			word = "recall",
			phonetic = "/rɪˈkɔːl/",
			audioUrl = null,
			options = listOf(
				ReviewOption(id = "recall-1", text = "стойкость"),
				ReviewOption(id = "recall-2", text = "вспоминать; отзыв"),
			),
			correctOptionId = "recall-2",
		),
	)

	@Test
	fun `invoke EXPECT repository get session`() = runTest {
		whenever(reviewRepository.getSession()) doReturn words

		getReviewSessionUseCase()

		verify(reviewRepository).getSession()
	}

	@Test
	fun `invoke EXPECT words from repository`() = runTest {
		whenever(reviewRepository.getSession()) doReturn words

		val actual = getReviewSessionUseCase()

		assertEquals(words, actual)
	}
}