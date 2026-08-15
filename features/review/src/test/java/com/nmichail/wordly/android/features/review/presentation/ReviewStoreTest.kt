package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.review.domain.entity.ReviewOption
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord
import com.nmichail.wordly.android.features.review.domain.usecase.GetReviewSessionUseCase
import com.nmichail.wordly.android.features.review.domain.usecase.SubmitReviewAnswerUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class ReviewStoreTest {

	private val getReviewSessionUseCase: GetReviewSessionUseCase = mock()
	private val submitReviewAnswerUseCase: SubmitReviewAnswerUseCase = mock()

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
	private val secondWord = ReviewWord(
		id = "resilience",
		word = "resilience",
		phonetic = "/rɪˈzɪliəns/",
		audioUrl = null,
		options = listOf(
			ReviewOption(id = "resilience-1", text = "стойкость"),
			ReviewOption(id = "resilience-2", text = "вспоминать"),
			ReviewOption(id = "resilience-3", text = "рычаг"),
			ReviewOption(id = "resilience-4", text = "скучный"),
		),
		correctOptionId = "resilience-1",
	)
	private val words = listOf(firstWord, secondWord)
	private val exception = IOException("network")
	private val lifecycle = createTestLifecycle()

	@AfterEach
	fun tearDown() {
		lifecycle.destroy()
	}

	@Test
	fun `load session success EXPECT content with first word`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words

		val store = createStore()

		assertEquals(content(), store.state)
	}

	@Test
	fun `load session empty EXPECT error`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn emptyList()

		val store = createStore()

		assertEquals(ReviewStore.State.Error, store.state)
	}

	@Test
	fun `load session failure EXPECT error`() = runTest {
		whenever(getReviewSessionUseCase()) doThrowSafe exception

		val store = createStore()

		assertEquals(ReviewStore.State.Error, store.state)
	}

	@Test
	fun `select correct option EXPECT answer revealed as correct`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()

		store.accept(ReviewStore.Intent.SelectOption(firstWord.correctOptionId))

		assertEquals(
			content(
				selectedOptionId = firstWord.correctOptionId,
				answerRevealed = true,
				correct = true,
			),
			store.state,
		)
	}

	@Test
	fun `select wrong option EXPECT answer revealed as incorrect`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()

		store.accept(ReviewStore.Intent.SelectOption("recall-1"))

		assertEquals(
			content(
				selectedOptionId = "recall-1",
				answerRevealed = true,
				correct = false,
			),
			store.state,
		)
	}

	@Test
	fun `select option when already revealed EXPECT state unchanged`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()
		store.accept(ReviewStore.Intent.SelectOption(firstWord.correctOptionId))
		val revealed = store.state

		store.accept(ReviewStore.Intent.SelectOption("recall-1"))

		assertEquals(revealed, store.state)
	}

	@Test
	fun `continue after correct answer EXPECT submit with correct true`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()
		store.accept(ReviewStore.Intent.SelectOption(firstWord.correctOptionId))

		store.accept(ReviewStore.Intent.Continue)

		verify(submitReviewAnswerUseCase).invoke(
			firstWord.id,
			firstWord.correctOptionId,
			true,
		)
	}

	@Test
	fun `continue after wrong answer EXPECT submit with correct false`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()
		store.accept(ReviewStore.Intent.SelectOption("recall-1"))

		store.accept(ReviewStore.Intent.Continue)

		verify(submitReviewAnswerUseCase).invoke(
			firstWord.id,
			"recall-1",
			false,
		)
	}

	@Test
	fun `continue after correct answer EXPECT next word`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()
		store.accept(ReviewStore.Intent.SelectOption(firstWord.correctOptionId))

		store.accept(ReviewStore.Intent.Continue)

		assertEquals(
			content(
				currentIndex = 1,
				correctCount = 1,
			),
			store.state,
		)
	}

	@Test
	fun `continue on last word EXPECT finished content`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()
		store.accept(ReviewStore.Intent.SelectOption(firstWord.correctOptionId))
		store.accept(ReviewStore.Intent.Continue)
		store.accept(ReviewStore.Intent.SelectOption("resilience-2"))

		store.accept(ReviewStore.Intent.Continue)

		assertEquals(
			content(
				currentIndex = 1,
				selectedOptionId = "resilience-2",
				answerRevealed = true,
				correct = false,
				correctCount = 1,
				finished = true,
			),
			store.state,
		)
	}

	@Test
	fun `continue before answer revealed EXPECT submit not invoked`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()

		store.accept(ReviewStore.Intent.Continue)

		verify(submitReviewAnswerUseCase, never()).invoke(any(), any(), any())
	}

	@Test
	fun `retry after error EXPECT session reload`() = runTest {
		whenever(getReviewSessionUseCase())
			.doThrowSafe(exception)
			.thenReturn(words)
		val store = createStore()

		store.accept(ReviewStore.Intent.Retry)

		assertEquals(content(), store.state)
	}

	@Test
	fun `close EXPECT close label`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(ReviewStore.Intent.Close)

		assertEquals(ReviewStore.Label.Close, labelsChannel.receive())
	}

	@Test
	fun `finish EXPECT close label`() = runTest {
		whenever(getReviewSessionUseCase()) doReturn words
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(ReviewStore.Intent.Finish)

		assertEquals(ReviewStore.Label.Close, labelsChannel.receive())
	}

	private fun content(
		currentIndex: Int = 0,
		selectedOptionId: String? = null,
		answerRevealed: Boolean = false,
		correct: Boolean = false,
		correctCount: Int = 0,
		submitting: Boolean = false,
		finished: Boolean = false,
	): ReviewStore.State.Content =
		ReviewStore.State.Content(
			words = words,
			currentIndex = currentIndex,
			currentWord = words[currentIndex],
			totalCount = words.size,
			progressIndex = currentIndex + 1,
			selectedOptionId = selectedOptionId,
			answerRevealed = answerRevealed,
			correct = correct,
			correctCount = correctCount,
			submitting = submitting,
			finished = finished,
		)

	private fun createStore(): ReviewStore =
		ReviewStoreFactory(
			getReviewSessionUseCase = getReviewSessionUseCase,
			submitReviewAnswerUseCase = submitReviewAnswerUseCase,
		).create()
}
