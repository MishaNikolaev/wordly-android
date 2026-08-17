package com.nmichail.wordly.android.features.cards.domain.usecase

import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import com.nmichail.wordly.android.features.cards.domain.entity.CardsLevelBanner
import com.nmichail.wordly.android.features.cards.domain.entity.CardsSection
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
class GetCardsUseCaseTest {

	private val cardsRepository: CardsRepository = mock()
	private val getCardsUseCase = GetCardsUseCase(cardsRepository)

	private val cards = Cards(
		title = "Карточки",
		searchPlaceholder = "Поиск темы или сферы",
		levelBanner = CardsLevelBanner(
			text = "Ваш уровень / темы подбираются под него",
			levelLabel = "B2",
			levels = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
		),
		sections = listOf(
			CardsSection(
				title = "Под ваш уровень · B2",
				items = listOf(
					CardsItem(
						id = "science",
						title = "Наука",
						subtitle = "8 слов",
						badge = "B2",
						imageUrl = null,
					),
				),
			),
		),
	)

	@Test
	fun `invoke EXPECT repository get cards`() = runTest {
		whenever(cardsRepository.getCards()) doReturn cards

		getCardsUseCase()

		verify(cardsRepository).getCards()
	}

	@Test
	fun `invoke EXPECT cards from repository`() = runTest {
		whenever(cardsRepository.getCards()) doReturn cards

		val actual = getCardsUseCase()

		assertEquals(cards, actual)
	}
}