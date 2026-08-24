package com.nmichail.wordly.android.features.books.reader.presentation

import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.reader.domain.usecase.GetBookContentUseCase
import com.nmichail.wordly.android.features.books.reader.domain.usecase.GetBookTranslationUseCase
import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordLookup
import com.nmichail.wordly.android.features.words.domain.usecase.AddWordUseCase
import com.nmichail.wordly.android.features.words.domain.usecase.LookupWordUseCase
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class BookReaderStoreTest {

	private val getBookContentUseCase: GetBookContentUseCase = mock()
	private val getBookTranslationUseCase: GetBookTranslationUseCase = mock()
	private val lookupWordUseCase: LookupWordUseCase = mock()
	private val addWordUseCase: AddWordUseCase = mock()
	private val lifecycle = createTestLifecycle()
	private val exception = IOException("network")

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
						type = BookTextSegmentType.TEXT,
						text = "Once when I was six years old ",
						id = null,
						definition = null,
					),
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

	@AfterEach
	fun tearDown() {
		lifecycle.destroy()
	}

	@Test
	fun `load book success EXPECT content`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book

		val store = createStore()

		assertEquals(content(), store.state)
	}

	@Test
	fun `load book failure EXPECT error`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doThrowSafe exception

		val store = createStore()

		assertEquals(BookReaderStore.State.Error, store.state)
	}

	@Test
	fun `retry after error EXPECT content`() = runTest {
		whenever(getBookContentUseCase("little-prince"))
			.doThrowSafe(exception)
			.thenReturn(book)
		val store = createStore()

		store.accept(BookReaderStore.Intent.Retry)

		assertEquals(content(), store.state)
	}

	@Test
	fun `select word EXPECT selected definition`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		val store = createStore()

		store.accept(BookReaderStore.Intent.SelectWord(wordId = "prince"))

		assertEquals(content(selectedWord = wordDefinition), store.state)
	}

	@Test
	fun `select free word EXPECT lookup definition`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		whenever(lookupWordUseCase("planet")) doReturn WordLookup(
			word = "planet",
			phonetic = "/ˈplænɪt/",
			translation = null,
			definition = "a celestial body",
			examples = listOf(WordExample(text = "Earth is a planet.", translation = null)),
			difficulty = 2,
		)
		val store = createStore()

		store.accept(BookReaderStore.Intent.SelectWord(wordId = "w:planet"))

		assertEquals(
			content(
				selectedWord = BookWordDefinition(
					word = "planet",
					phonetic = "/ˈplænɪt/",
					translation = null,
					partOfSpeech = null,
					example = "Earth is a planet.",
					definition = "a celestial body",
				),
			),
			store.state,
		)
	}

	@Test
	fun `dismiss word dialog EXPECT selected word cleared`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		val store = createStore()
		store.accept(BookReaderStore.Intent.SelectWord(wordId = "prince"))

		store.accept(BookReaderStore.Intent.DismissWordDialog)

		assertEquals(content(), store.state)
	}

	@Test
	fun `add word to card EXPECT dialog and label`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		whenever(addWordUseCase(any())).thenReturn(Unit)
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)
		store.accept(BookReaderStore.Intent.SelectWord(wordId = "prince"))

		store.accept(BookReaderStore.Intent.AddWordToCard)

		assertEquals(
			content(
				selectedWord = wordDefinition,
				showWordAddedDialog = true,
			),
			store.state,
		)
		assertEquals(
			BookReaderStore.Label.AddWordToCard(definition = wordDefinition),
			labelsChannel.receive(),
		)
		verify(addWordUseCase).invoke(
			NewWord(
				word = "prince",
				phonetic = "/prɪns/",
				translation = "принц",
				definition = "noun",
				examples = listOf(
					WordExample(
						text = "The little prince lived on a tiny planet.",
						translation = null,
					),
				),
				tagIds = emptyList(),
				difficulty = 2,
			),
		)
	}

	@Test
	fun `dismiss word added dialog EXPECT selection cleared`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		whenever(addWordUseCase(any())).thenReturn(Unit)
		val store = createStore()
		store.accept(BookReaderStore.Intent.SelectWord(wordId = "prince"))
		store.accept(BookReaderStore.Intent.AddWordToCard)

		store.accept(BookReaderStore.Intent.DismissWordAddedDialog)

		assertEquals(content(), store.state)
	}

	@Test
	fun `close EXPECT close label`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		val store = createStore()
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(BookReaderStore.Intent.Close)

		assertEquals(BookReaderStore.Label.Close, labelsChannel.receive())
	}

	private fun content(
		translation: BookTranslation? = null,
		translationVisible: Boolean = false,
		translating: Boolean = false,
		selectedWord: BookWordDefinition? = null,
		wordLookupLoading: Boolean = false,
		showWordAddedDialog: Boolean = false,
	): BookReaderStore.State.Content =
		BookReaderStore.State.Content(
			book = book,
			translation = translation,
			translationVisible = translationVisible,
			translating = translating,
			selectedWord = selectedWord,
			wordLookupLoading = wordLookupLoading,
			showWordAddedDialog = showWordAddedDialog,
		)

	private fun createStore(): BookReaderStore =
		BookReaderStoreFactory(
			getBookContentUseCase = getBookContentUseCase,
			getBookTranslationUseCase = getBookTranslationUseCase,
			lookupWordUseCase = lookupWordUseCase,
			addWordUseCase = addWordUseCase,
		).create(bookId = "little-prince")
}
