package com.nmichail.wordly.android.features.constructor.practice.presentation

import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorPhrase
import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorWord
import com.nmichail.wordly.android.features.constructor.practice.domain.usecase.GetConstructorSessionUseCase
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
class ConstructorPracticeStoreTest {

	private val getConstructorSessionUseCase: GetConstructorSessionUseCase = mock()

	private val firstWords = listOf(
		ConstructorWord(id = "w1", text = "I"),
		ConstructorWord(id = "w2", text = "know"),
		ConstructorWord(id = "w3", text = "nothing"),
		ConstructorWord(id = "w4", text = "never"),
	)
	private val secondWords = listOf(
		ConstructorWord(id = "d1", text = "I"),
		ConstructorWord(id = "d2", text = "think"),
		ConstructorWord(id = "d3", text = "am"),
		ConstructorWord(id = "d4", text = "so"),
	)
	private val firstPhrase = ConstructorPhrase(
		id = "socrates-1",
		question = "Я знаю, что ничего не знаю.",
		author = "Сократ",
		words = firstWords,
		correctOrder = listOf("w1", "w2", "w3"),
	)
	private val secondPhrase = ConstructorPhrase(
		id = "descartes-1",
		question = "Я мыслю, следовательно, я существую.",
		author = "Декарт",
		words = secondWords,
		correctOrder = listOf("d1", "d2", "d3"),
	)
	private val session = ConstructorSession(
		themeId = "philosophy",
		themeTitle = "Философские мысли",
		phrases = listOf(firstPhrase, secondPhrase),
	)
	private val exception = IOException("network")
	private val lifecycle = createTestLifecycle()

	@AfterEach
	fun tearDown() {
		lifecycle.destroy()
	}

	@Test
	fun `load session success EXPECT in progress with first phrase`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session

		val store = createStore()

		assertEquals(inProgress(), store.state)
	}

	@Test
	fun `load session empty EXPECT error`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session.copy(phrases = emptyList())

		val store = createStore()

		assertEquals(ConstructorPracticeStore.State.Error, store.state)
	}

	@Test
	fun `load session failure EXPECT error`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doThrowSafe exception

		val store = createStore()

		assertEquals(ConstructorPracticeStore.State.Error, store.state)
	}

	@Test
	fun `place word EXPECT moved to answer`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()

		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w1"))

		assertEquals(
			inProgress(
				bank = phraseBank().filterNot { it.id == "w1" },
				answer = listOf(firstWords.first { it.id == "w1" }),
			),
			store.state,
		)
	}

	@Test
	fun `remove word EXPECT returned to bank`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w1"))

		store.accept(ConstructorPracticeStore.Intent.RemoveWord("w1"))

		assertEquals(
			inProgress(
				bank = phraseBank(),
			),
			store.state,
		)
	}

	@Test
	fun `move answer word EXPECT reordered`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w1"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w2"))

		store.accept(ConstructorPracticeStore.Intent.MoveAnswerWord(fromIndex = 0, toIndex = 1))

		assertEquals(
			inProgress(
				bank = phraseBank().filterNot { it.id == "w1" || it.id == "w2" },
				answer = listOf(
					firstWords.first { it.id == "w2" },
					firstWords.first { it.id == "w1" },
				),
			),
			store.state,
		)
	}

	@Test
	fun `check correct order EXPECT revealed as correct`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w1"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w2"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w3"))

		store.accept(ConstructorPracticeStore.Intent.Check)

		assertEquals(
			inProgress(
				bank = phraseBank().filterNot { it.id == "w1" || it.id == "w2" || it.id == "w3" },
				answer = listOf(
					firstWords.first { it.id == "w1" },
					firstWords.first { it.id == "w2" },
					firstWords.first { it.id == "w3" },
				),
				checkResult = true,
			),
			store.state,
		)
	}

	@Test
	fun `check wrong order EXPECT revealed as incorrect`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w3"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w1"))

		store.accept(ConstructorPracticeStore.Intent.Check)

		assertEquals(
			inProgress(
				bank = phraseBank().filterNot { it.id == "w3" || it.id == "w1" },
				answer = listOf(
					firstWords.first { it.id == "w3" },
					firstWords.first { it.id == "w1" },
				),
				checkResult = false,
			),
			store.state,
		)
	}

	@Test
	fun `continue after correct answer EXPECT next phrase`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w1"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w2"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w3"))
		store.accept(ConstructorPracticeStore.Intent.Check)

		store.accept(ConstructorPracticeStore.Intent.Continue)

		assertEquals(
			inProgress(
				currentIndex = 1,
				correctCount = 1,
			),
			store.state,
		)
	}

	@Test
	fun `continue on last phrase EXPECT finished`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w1"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w2"))
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("w3"))
		store.accept(ConstructorPracticeStore.Intent.Check)
		store.accept(ConstructorPracticeStore.Intent.Continue)
		store.accept(ConstructorPracticeStore.Intent.PlaceWord("d4"))
		store.accept(ConstructorPracticeStore.Intent.Check)

		store.accept(ConstructorPracticeStore.Intent.Continue)

		assertEquals(
			finished(correctCount = 1),
			store.state,
		)
	}

	@Test
	fun `continue before answer revealed EXPECT state unchanged`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		val before = store.state

		store.accept(ConstructorPracticeStore.Intent.Continue)

		assertEquals(before, store.state)
	}

	@Test
	fun `retry after error EXPECT session reload`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy"))
			.doThrowSafe(exception)
			.thenReturn(session)
		val store = createStore()

		store.accept(ConstructorPracticeStore.Intent.Retry)

		assertEquals(inProgress(), store.state)
	}

	@Test
	fun `close EXPECT close label`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(ConstructorPracticeStore.Intent.Close)

		assertEquals(ConstructorPracticeStore.Label.Close, labelsChannel.receive())
	}

	@Test
	fun `finish EXPECT close label`() = runTest {
		whenever(getConstructorSessionUseCase("philosophy")) doReturn session
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(ConstructorPracticeStore.Intent.Finish)

		assertEquals(ConstructorPracticeStore.Label.Close, labelsChannel.receive())
	}

	private fun phraseBank(index: Int = 0): List<ConstructorWord> {
		val phrase = session.phrases[index]
		return phrase.words.shuffledBank("${session.themeId}:${phrase.id}")
	}

	private fun inProgress(
		currentIndex: Int = 0,
		bank: List<ConstructorWord> = phraseBank(currentIndex),
		answer: List<ConstructorWord> = emptyList(),
		checkResult: Boolean? = null,
		correctCount: Int = 0,
	): ConstructorPracticeStore.State.Content.InProgress =
		ConstructorPracticeStore.State.Content.InProgress(
			session = session,
			currentIndex = currentIndex,
			bank = bank,
			answer = answer,
			checkResult = checkResult,
			correctCount = correctCount,
			totalCount = session.phrases.size,
		)

	private fun finished(
		correctCount: Int,
	): ConstructorPracticeStore.State.Content.Finished =
		ConstructorPracticeStore.State.Content.Finished(
			correctCount = correctCount,
			totalCount = session.phrases.size,
		)

	private fun createStore(): ConstructorPracticeStore =
		ConstructorPracticeStoreFactory(
			getConstructorSessionUseCase = getConstructorSessionUseCase,
		).create(themeId = "philosophy")
}
