package com.nmichail.wordly.android.features.books.reader.domain.usecase

import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslatedParagraph
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.domain.repository.BookReaderRepository
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
class GetBookTranslationUseCaseTest {

	private val bookReaderRepository: BookReaderRepository = mock()
	private val getBookTranslationUseCase = GetBookTranslationUseCase(bookReaderRepository)

	private val translation = BookTranslation(
		paragraphs = listOf(
			BookTranslatedParagraph(
				id = "p1",
				text = "Когда мне было шесть лет, я увидел великолепную картинку в книге про джунгли.",
			),
		),
	)

	@Test
	fun `invoke EXPECT repository get translation`() = runTest {
		whenever(bookReaderRepository.getBookTranslation("little-prince")) doReturn translation

		getBookTranslationUseCase("little-prince")

		verify(bookReaderRepository).getBookTranslation("little-prince")
	}

	@Test
	fun `invoke EXPECT translation from repository`() = runTest {
		whenever(bookReaderRepository.getBookTranslation("little-prince")) doReturn translation

		val actual = getBookTranslationUseCase("little-prince")

		assertEquals(translation, actual)
	}
}
