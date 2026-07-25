package com.nmichail.wordly.android.features.cards.presentation

import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestComponentContext
import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import com.nmichail.wordly.android.features.cards.domain.entity.CardsLevelBanner
import com.nmichail.wordly.android.features.cards.domain.entity.CardsSection
import com.nmichail.wordly.android.features.cards.domain.usecase.GetCardsUseCase
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
class DefaultCardsComponentTest {

	private val getCardsUseCase: GetCardsUseCase = mock()
	private val cardsRouter: CardsRouter = mock()
	private val onCardClick: (CardsItem) -> Unit = mock()

	private val scienceItem = CardsItem(
		id = "science",
		title = "Наука",
		subtitle = "8 слов",
		badge = "B2",
		imageUrl = null,
	)
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
				items = listOf(scienceItem),
			),
		),
	)

	private lateinit var component: DefaultCardsComponent

	@BeforeEach
	fun setUp() = runTest {
		whenever(getCardsUseCase()) doReturn cards
		component = DefaultCardsComponent(
			componentContext = createTestComponentContext(),
			cardsStoreFactory = CardsStoreFactory(
				getCardsUseCase = getCardsUseCase,
			),
			cardsRouter = cardsRouter,
			onCardClick = onCardClick,
		)
	}

	@Test
	fun `back EXPECT navigate back`() = runTest {
		component.handleBack()

		verify(cardsRouter).navigateBack()
	}

	@Test
	fun `card click EXPECT on card click`() = runTest {
		component.handleCardClick(scienceItem.id)

		verify(onCardClick).invoke(scienceItem)
	}

	@Test
	fun `level change EXPECT updated level in state`() = runTest {
		component.handleLevelChange("C1")

		val content = component.model.value as CardsComponent.State.Content
		assertEquals("C1", content.levelBanner?.levelLabel)
	}
}
