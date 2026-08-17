package com.nmichail.wordly.android.features.books.reader.domain.usecase

import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookWordDefinition
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
class GetBookContentUseCaseTest {

	private val bookReaderRepository: BookReaderRepository = mock()
	private val getBookContentUseCase = GetBookContentUseCase(bookReaderRepository)

	private val content = BookContent(
		id = "little-prince",
		title = "The Little Prince",
		author = "Antoine de Saint-Exupéry",
		coverUrl = null,
		paragraphs = listOf(
			BookParagraph(
				id = "p1",
				segments = listOf(
					BookTextSegment(
						type = BookTextSegmentType.TEXT,
						text = "Once when I was six years old ",
						id = null,
						definition = null,
					),
					BookTextSegment(
						type = BookTextSegmentType.LOOKUP_WORD,
						text = "prince",
						id = "prince",
						definition = BookWordDefinition(
							word = "prince",
							phonetic = "/prɪns/",
							translation = "принц",
							partOfSpeech = "noun",
							example = "The little prince lived on a tiny planet.",
						),
					),
				),
			),
		),
	)

	@Test
	fun `invoke EXPECT repository get book`() = runTest {
		whenever(bookReaderRepository.getBookContent("little-prince")) doReturn content

		getBookContentUseCase("little-prince")

		verify(bookReaderRepository).getBookContent("little-prince")
	}

	@Test
	fun `invoke EXPECT book from repository`() = runTest {
		whenever(bookReaderRepository.getBookContent("little-prince")) doReturn content

		val actual = getBookContentUseCase("little-prince")

		assertEquals(content, actual)
	}
}
