package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.domain.usecase.GetBookContentUseCase
import com.nmichail.wordly.android.features.books.domain.usecase.GetBookTranslationUseCase
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class BookReaderStoreTest {

	private val getBookContentUseCase: GetBookContentUseCase = mock()
	private val getBookTranslationUseCase: GetBookTranslationUseCase = mock()
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

		assertEquals(BookReaderComponent.State.Error, store.state)
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
	fun `select unknown word EXPECT state unchanged`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
		val store = createStore()

		store.accept(BookReaderStore.Intent.SelectWord(wordId = "unknown"))

		assertEquals(content(), store.state)
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
			BookReaderComponent.Label.AddWordToCard(definition = wordDefinition),
			labelsChannel.receive(),
		)
	}

	@Test
	fun `dismiss word added dialog EXPECT selection cleared`() = runTest {
		whenever(getBookContentUseCase("little-prince")) doReturn book
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

		assertEquals(BookReaderComponent.Label.Close, labelsChannel.receive())
	}

	private fun content(
		translation: BookTranslation? = null,
		isTranslationVisible: Boolean = false,
		isTranslating: Boolean = false,
		selectedWord: BookWordDefinition? = null,
		showWordAddedDialog: Boolean = false,
	): BookReaderComponent.State.Content =
		BookReaderComponent.State.Content(
			book = book,
			translation = translation,
			isTranslationVisible = isTranslationVisible,
			isTranslating = isTranslating,
			selectedWord = selectedWord,
			showWordAddedDialog = showWordAddedDialog,
		)

	private fun createStore(): BookReaderStore =
		BookReaderStoreFactory(
			getBookContentUseCase = getBookContentUseCase,
			getBookTranslationUseCase = getBookTranslationUseCase,
		).create(bookId = "little-prince")
}