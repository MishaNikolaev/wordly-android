package com.nmichail.wordly.android.features.cards.presentation.detail

import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestComponentContext
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeOption
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.domain.usecase.GetCardSessionUseCase
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
class DefaultCardPracticeComponentTest {

	private val getCardSessionUseCase: GetCardSessionUseCase = mock()
	private val cardPracticeRouter: CardPracticeRouter = mock()

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
	private val words = listOf(firstWord)

	private lateinit var component: DefaultCardPracticeComponent

	@BeforeEach
	fun setUp() = runTest {
		whenever(getCardSessionUseCase("science")) doReturn words
		component = DefaultCardPracticeComponent(
			componentContext = createTestComponentContext(),
			cardId = "science",
			cardPracticeStoreFactory = CardPracticeStoreFactory(
				getCardSessionUseCase = getCardSessionUseCase,
			),
			cardPracticeRouter = cardPracticeRouter,
		)
	}

	@Test
	fun `init EXPECT in progress state`() {
		assertEquals(inProgress(), component.model.value)
	}

	@Test
	fun `select correct option EXPECT answer revealed`() {
		component.handleSelectOption(firstWord.correctOptionId)

		assertEquals(
			inProgress(
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

		verify(cardPracticeRouter).navigateBack()
	}

	@Test
	fun `finish EXPECT navigate back`() = runTest {
		component.handleFinish()

		verify(cardPracticeRouter).navigateBack()
	}

	private fun inProgress(
		selectedOptionId: String? = null,
		answerRevealed: Boolean = false,
		correct: Boolean = false,
	): CardPracticeStore.State.InProgress =
		CardPracticeStore.State.InProgress(
			words = words,
			currentIndex = 0,
			currentWord = firstWord,
			totalCount = 1,
			progressIndex = 1,
			selectedOptionId = selectedOptionId,
			answerRevealed = answerRevealed,
			correct = correct,
			correctCount = 0,
		)
}