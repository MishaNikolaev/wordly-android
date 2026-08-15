package com.nmichail.wordly.android.features.books.presentation.detail

import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestComponentContext
import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.domain.usecase.GetBookContentUseCase
import com.nmichail.wordly.android.features.books.domain.usecase.GetBookTranslationUseCase
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
class DefaultBookReaderComponentTest {

	private val getBookContentUseCase: GetBookContentUseCase = mock()
	private val getBookTranslationUseCase: GetBookTranslationUseCase = mock()
	private val bookReaderRouter: BookReaderRouter = mock()
	private val onAddWordToCard: (BookWordDefinition) -> Unit = mock()

	private val wordDefinition = BookWordDefinition(
		word = "prince",
		phonetic = "/prɪns/",
		translation = "принц",
		partOfSpeech = "noun",
		example = "The little prince lived on a tiny planet.",
	)
	private val book = BookContent(
		id = "little-prince",
		title = "The Little Prince",
		author = "Antoine de Saint-Exupéry",
		coverUrl = null,
		paragraphs = listOf(
			BookParagraph(
				id = "p1",
				segments = listOf(
					BookTextSegment(
						type = BookTextSegmentType.LOOKUP_WORD,
						text = "prince",
						id = "prince",
						definition = wordDefinition,
					),
				),
			),
		),
	)

	private lateinit var component: DefaultBookReaderComponent

	@BeforeEach
	fun setUp() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		component = DefaultBookReaderComponent(
			componentContext = createTestComponentContext(),
			bookId = "little-prince",
			bookReaderStoreFactory = BookReaderStoreFactory(
				getBookContentUseCase = getBookContentUseCase,
				getBookTranslationUseCase = getBookTranslationUseCase,
			),
			bookReaderRouter = bookReaderRouter,
			onAddWordToCard = onAddWordToCard,
		)
	}

	@Test
	fun `init EXPECT content state`() {
		assertEquals(
			BookReaderStore.State.Content(
				book = book,
				translation = null,
				translationVisible = false,
				translating = false,
				selectedWord = null,
				showWordAddedDialog = false,
			),
			component.model.value,
		)
	}

	@Test
	fun `select word EXPECT selected definition`() {
		component.handleSelectWord(wordId = "prince")

		assertEquals(
			BookReaderStore.State.Content(
				book = book,
				translation = null,
				translationVisible = false,
				translating = false,
				selectedWord = wordDefinition,
				showWordAddedDialog = false,
			),
			component.model.value,
		)
	}

	@Test
	fun `close EXPECT navigate back`() = runTest {
		component.handleClose()

		verify(bookReaderRouter).navigateBack()
	}

	@Test
	fun `add word to card EXPECT callback`() = runTest {
		component.handleSelectWord(wordId = "prince")

		component.handleAddWordToCard()

		verify(onAddWordToCard)(wordDefinition)
	}
}