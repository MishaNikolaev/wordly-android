package com.nmichail.wordly.android.features.review.presentation

import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestComponentContext
import com.nmichail.wordly.android.features.review.domain.entity.ReviewOption
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord
import com.nmichail.wordly.android.features.review.domain.usecase.GetReviewSessionUseCase
import com.nmichail.wordly.android.features.review.domain.usecase.SubmitReviewAnswerUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class DefaultReviewComponentTest {

	private val getReviewSessionUseCase: GetReviewSessionUseCase = mock()
	private val submitReviewAnswerUseCase: SubmitReviewAnswerUseCase = mock()
	private val reviewRouter: ReviewRouter = mock()

	private val firstWord = ReviewWord(
		id = "recall",
		word = "recall",
		phonetic = "/rɪˈkɔːl/",
		audioUrl = null,
		options = listOf(
			ReviewOption(id = "recall-1", text = "стойкость"),
			ReviewOption(id = "recall-2", text = "вспоминать; отзыв"),
			ReviewOption(id = "recall-3", text = "использовать"),
			ReviewOption(id = "recall-4", text = "обыденный"),
		),
		correctOptionId = "recall-2",
	)
	private val words = listOf(firstWord)

	private lateinit var component: DefaultReviewComponent

	@BeforeEach
	fun setUp() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		component = DefaultReviewComponent(
			componentContext = createTestComponentContext(),
			reviewStoreFactory = ReviewStoreFactory(
				getReviewSessionUseCase = getReviewSessionUseCase,
				submitReviewAnswerUseCase = submitReviewAnswerUseCase,
			),
			reviewRouter = reviewRouter,
		)
	}

	@Test
	fun `init EXPECT content state`() {
		assertEquals(content(), component.model.value)
	}

	@Test
	fun `select correct option EXPECT answer revealed`() {
		component.handleSelectOption(firstWord.correctOptionId)

		assertEquals(
			content(
				selectedOptionId = firstWord.correctOptionId,
				answerRevealed = true,
				correct = true,
			),
			component.model.value,
		)
	}

	@Test
	fun `close EXPECT navigate back`() = runTest {
		component.handleClose()

		verify(reviewRouter).navigateBack()
	}

	@Test
	fun `finish EXPECT navigate back`() = runTest {
		component.handleFinish()

		verify(reviewRouter).navigateBack()
	}

	private fun content(
		selectedOptionId: String? = null,
		answerRevealed: Boolean = false,
		correct: Boolean = false,
	): ReviewStore.State.Content.InProgress =
		ReviewStore.State.Content.InProgress(
			words = words,
			currentIndex = 0,
			currentWord = firstWord,
			totalCount = 1,
			progressIndex = 1,
			selectedOptionId = selectedOptionId,
			answerRevealed = answerRevealed,
			correct = correct,
			correctCount = 0,
			submitting = false,
		)
}
