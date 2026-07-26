package com.nmichail.wordly.android.features.books.domain.usecase

import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.features.books.domain.entity.BooksLevelBanner
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection
import com.nmichail.wordly.android.features.books.domain.repository.BooksRepository
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
class GetBooksCatalogUseCaseTest {

	private val booksRepository: BooksRepository = mock()
	private val getBooksCatalogUseCase = GetBooksCatalogUseCase(booksRepository)

	private val catalog = BooksCatalog(
		title = "Книги",
		searchPlaceholder = "Поиск книги",
		levelBanner = BooksLevelBanner(
			text = "Ваш уровень / книги подбираются под него",
			levelLabel = "B1",
			levels = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
		),
		sections = listOf(
			BooksSection(
				title = "Под ваш уровень · B1",
				items = listOf(
					BooksItem(
						id = "little-prince",
						title = "The Little Prince",
						subtitle = "Antoine de Saint-Exupéry",
						badge = "B1",
						imageUrl = null,
					),
				),
			),
		),
	)

	@Test
	fun `invoke EXPECT repository get catalog`() = runTest {
		whenever(booksRepository.getCatalog()) doReturn catalog

		getBooksCatalogUseCase()

		verify(booksRepository).getCatalog()
	}

	@Test
	fun `invoke EXPECT catalog from repository`() = runTest {
		whenever(booksRepository.getCatalog()) doReturn catalog

		val actual = getBooksCatalogUseCase()

		assertEquals(catalog, actual)
	}
}
