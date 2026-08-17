package com.nmichail.wordly.android.features.cards.presentation

import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.shared.englishlevel.domain.usecase.UpdateEnglishLevelUseCase
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import com.nmichail.wordly.android.features.cards.domain.entity.CardsLevelBanner
import com.nmichail.wordly.android.features.cards.domain.entity.CardsSection
import com.nmichail.wordly.android.features.cards.domain.usecase.GetCardsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class CardsStoreTest {

	private val getCardsUseCase: GetCardsUseCase = mock()
	private val updateEnglishLevelUseCase: UpdateEnglishLevelUseCase = mock()

	private val scienceItem = CardsItem(
		id = "science",
		title = "Наука",
		subtitle = "8 слов",
		badge = "B2",
		imageUrl = null,
	)
	private val journalismItem = CardsItem(
		id = "journalism",
		title = "Журналистика",
		subtitle = "10 слов",
		badge = "B2",
		imageUrl = null,
	)
	private val engineeringItem = CardsItem(
		id = "engineering",
		title = "Инженерия",
		subtitle = "9 слов",
		badge = "B1",
		imageUrl = null,
	)
	private val levelSections = listOf(
		CardsSection(
			title = "Под ваш уровень · B2",
			items = listOf(scienceItem, journalismItem),
		),
		CardsSection(
			title = "Другие уровни",
			items = listOf(engineeringItem),
		),
	)
	private val cards = Cards(
		title = "Карточки",
		searchPlaceholder = "Поиск темы или сферы",
		levelBanner = CardsLevelBanner(
			text = "Ваш уровень / темы подбираются под него",
			levelLabel = "B2",
			levels = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
		),
		sections = levelSections,
	)
	private val exception = IOException("network")
	private val lifecycle = createTestLifecycle()

	@AfterEach
	fun tearDown() {
		lifecycle.destroy()
	}

	@Test
	fun `load success EXPECT content state`() = runTest {
		whenever(getCardsUseCase()) doReturn cards

		val store = createStore()

		assertEquals(content(), store.state)
	}

	@Test
	fun `load failure EXPECT error`() = runTest {
		whenever(getCardsUseCase()) doThrowSafe exception

		val store = createStore()

		assertEquals(CardsStore.State.Error, store.state)
	}

	@Test
	fun `retry after error EXPECT content state`() = runTest {
		whenever(getCardsUseCase())
			.doThrowSafe(exception)
			.thenReturn(cards)
		val store = createStore()

		store.accept(CardsStore.Intent.Retry)

		assertEquals(content(), store.state)
	}

	@Test
	fun `search by title EXPECT filtered sections`() = runTest {
		whenever(getCardsUseCase()) doReturn cards
		val store = createStore()

		store.accept(CardsStore.Intent.ChangeSearchQuery("Наука"))

		assertEquals(
			content(
				searchQuery = "Наука",
				sections = listOf(
					CardsSection(
						title = "Под ваш уровень · B2",
						items = listOf(scienceItem),
					),
				),
			),
			store.state,
		)
	}

	@Test
	fun `back EXPECT close label`() = runTest {
		whenever(getCardsUseCase()) doReturn cards
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(CardsStore.Intent.Back)

		assertEquals(CardsStore.Label.Close, labelsChannel.receive())
	}

	@Test
	fun `select card EXPECT open card label`() = runTest {
		whenever(getCardsUseCase()) doReturn cards
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(CardsStore.Intent.SelectCard(scienceItem.id))

		assertEquals(CardsStore.Label.OpenCard(scienceItem), labelsChannel.receive())
	}

	@Test
	fun `change level EXPECT updated level label`() = runTest {
		whenever(getCardsUseCase()) doReturn cards
		val store = createStore()
		val updatedSections = listOf(
			CardsSection(
				title = "Под ваш уровень · C1",
				items = listOf(scienceItem, journalismItem),
			),
			CardsSection(
				title = "Другие уровни",
				items = listOf(engineeringItem),
			),
		)

		store.accept(CardsStore.Intent.ChangeLevel("C1"))

		verify(updateEnglishLevelUseCase).invoke("C1")
		assertEquals(
			content(
				allSections = updatedSections,
				sections = updatedSections,
			).copy(
				levelBanner = cards.levelBanner?.copy(levelLabel = "C1"),
			),
			store.state,
		)
	}

	private fun content(
		searchQuery: String = "",
		allSections: List<CardsSection> = levelSections,
		sections: List<CardsSection> = levelSections,
	): CardsStore.State.Content =
		CardsStore.State.Content(
			title = cards.title,
			searchQuery = searchQuery,
			searchPlaceholder = cards.searchPlaceholder,
			levelBanner = cards.levelBanner,
			allSections = allSections,
			sections = sections,
		)

	private fun createStore(): CardsStore =
		CardsStoreFactory(
			getCardsUseCase = getCardsUseCase,
			updateEnglishLevelUseCase = updateEnglishLevelUseCase,
		).create()
}
