package com.nmichail.wordly.android.features.cards.presentation.detail

import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeOption
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.domain.usecase.GetCardSessionUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class CardPracticeStoreTest {

	private val getCardSessionUseCase: GetCardSessionUseCase = mock()

	private val firstWord = CardPracticeWord(
		id = "hypothesis",
		word = "hypothesis",
		phonetic = "/haɪˈpɒθəsɪs/",
		audioUrl = null,
		options = listOf(
			CardPracticeOption(id = "hypothesis-1", text = "молекула"),
			CardPracticeOption(id = "hypothesis-2", text = "гипотеза"),
			CardPracticeOption(id = "hypothesis-3", text = "гравитация"),
			CardPracticeOption(id = "hypothesis-4", text = "эксперимент"),
		),
		correctOptionId = "hypothesis-2",
	)
	private val secondWord = CardPracticeWord(
		id = "molecule",
		word = "molecule",
		phonetic = "/ˈmɒlɪkjuːl/",
		audioUrl = null,
		options = listOf(
			CardPracticeOption(id = "molecule-1", text = "молекула"),
			CardPracticeOption(id = "molecule-2", text = "гипотеза"),
			CardPracticeOption(id = "molecule-3", text = "реакция"),
			CardPracticeOption(id = "molecule-4", text = "формула"),
		),
		correctOptionId = "molecule-1",
	)
	private val words = listOf(firstWord, secondWord)
	private val exception = IOException("network")
	private val lifecycle = createTestLifecycle()

	@AfterEach
	fun tearDown() {
		lifecycle.destroy()
	}

	@Test
	fun `load session success EXPECT in progress with first word`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words

		val store = createStore()

		assertEquals(inProgress(), store.state)
	}

	@Test
	fun `load session empty EXPECT error`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn emptyList()

		val store = createStore()

		assertEquals(CardPracticeStore.State.Error, store.state)
	}

	@Test
	fun `load session failure EXPECT error`() = runTest {
		whenever(getCardSessionUseCase("science")) doThrowSafe exception

		val store = createStore()

		assertEquals(CardPracticeStore.State.Error, store.state)
	}

	@Test
	fun `select correct option EXPECT answer revealed as correct`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()

		store.accept(CardPracticeStore.Intent.SelectOption(firstWord.correctOptionId))

		assertEquals(
			inProgress(
				selectedOptionId = firstWord.correctOptionId,
				answerRevealed = true,
				correct = true,
			),
			store.state,
		)
	}

	@Test
	fun `select wrong option EXPECT answer revealed as incorrect`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()

		store.accept(CardPracticeStore.Intent.SelectOption("hypothesis-1"))

		assertEquals(
			inProgress(
				selectedOptionId = "hypothesis-1",
				answerRevealed = true,
				correct = false,
			),
			store.state,
		)
	}

	@Test
	fun `select option when already revealed EXPECT state unchanged`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()
		store.accept(CardPracticeStore.Intent.SelectOption(firstWord.correctOptionId))
		val revealed = store.state

		store.accept(CardPracticeStore.Intent.SelectOption("hypothesis-1"))

		assertEquals(revealed, store.state)
	}

	@Test
	fun `continue after correct answer EXPECT next word`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()
		store.accept(CardPracticeStore.Intent.SelectOption(firstWord.correctOptionId))

		store.accept(CardPracticeStore.Intent.Continue)

		assertEquals(
			inProgress(
				currentIndex = 1,
				correctCount = 1,
			),
			store.state,
		)
	}

	@Test
	fun `continue on last word EXPECT finished`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()
		store.accept(CardPracticeStore.Intent.SelectOption(firstWord.correctOptionId))
		store.accept(CardPracticeStore.Intent.Continue)
		store.accept(CardPracticeStore.Intent.SelectOption("molecule-2"))

		store.accept(CardPracticeStore.Intent.Continue)

		assertEquals(
			CardPracticeStore.State.Finished(
				totalCount = 2,
				correctCount = 1,
			),
			store.state,
		)
	}

	@Test
	fun `continue before answer revealed EXPECT state unchanged`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()
		val before = store.state

		store.accept(CardPracticeStore.Intent.Continue)

		assertEquals(before, store.state)
	}

	@Test
	fun `retry after error EXPECT session reload`() = runTest {
		whenever(getCardSessionUseCase("science"))
			.doThrowSafe(exception)
			.thenReturn(words)
		val store = createStore()

		store.accept(CardPracticeStore.Intent.Retry)

		assertEquals(inProgress(), store.state)
	}

	@Test
	fun `close EXPECT close label`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(CardPracticeStore.Intent.Close)

		assertEquals(CardPracticeStore.Label.Close, labelsChannel.receive())
	}

	@Test
	fun `finish EXPECT close label`() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(CardPracticeStore.Intent.Finish)

		assertEquals(CardPracticeStore.Label.Close, labelsChannel.receive())
	}

	private fun inProgress(
		currentIndex: Int = 0,
		selectedOptionId: String? = null,
		answerRevealed: Boolean = false,
		correct: Boolean = false,
		correctCount: Int = 0,
	): CardPracticeStore.State.InProgress =
		CardPracticeStore.State.InProgress(
			words = words,
			currentIndex = currentIndex,
			currentWord = words[currentIndex],
			totalCount = words.size,
			progressIndex = currentIndex + 1,
			selectedOptionId = selectedOptionId,
			answerRevealed = answerRevealed,
			correct = correct,
			correctCount = correctCount,
		)

	private fun createStore(): CardPracticeStore =
		CardPracticeStoreFactory(
			getCardSessionUseCase = getCardSessionUseCase,
		).create(cardId = "science")
}