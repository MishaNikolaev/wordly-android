package com.nmichail.wordly.android.features.cards.domain.usecase

import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeOption
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.domain.repository.CardsRepository
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
class GetCardSessionUseCaseTest {

	private val cardsRepository: CardsRepository = mock()
	private val getCardSessionUseCase = GetCardSessionUseCase(cardsRepository)

	private val words = listOf(
		CardPracticeWord(
			id = "hypothesis",
			word = "hypothesis",
			phonetic = "/haɪˈpɒθəsɪs/",
			audioUrl = null,
			options = listOf(
				CardPracticeOption(id = "hypothesis-1", text = "молекула"),
				CardPracticeOption(id = "hypothesis-2", text = "гипотеза"),
			),
			correctOptionId = "hypothesis-2",
		),
	)

	@Test
	fun `invoke EXPECT repository get card session`() = runTest {
		whenever(cardsRepository.getCardSession("science")) doReturn words

		getCardSessionUseCase("science")

		verify(cardsRepository).getCardSession("science")
	}

	@Test
	fun `invoke EXPECT words from repository`() = runTest {
		whenever(cardsRepository.getCardSession("science")) doReturn words

		val actual = getCardSessionUseCase("science")

		assertEquals(words, actual)
	}
}