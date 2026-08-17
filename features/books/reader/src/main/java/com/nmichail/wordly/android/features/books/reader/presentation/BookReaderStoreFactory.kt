package com.nmichail.wordly.android.features.books.reader.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.reader.domain.usecase.GetBookContentUseCase
import com.nmichail.wordly.android.features.books.reader.domain.usecase.GetBookTranslationUseCase
import javax.inject.Inject

internal class BookReaderStoreFactory @Inject constructor(
    private val getBookContentUseCase: GetBookContentUseCase,
    private val getBookTranslationUseCase: GetBookTranslationUseCase,
) {

    private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

    fun create(bookId: String): BookReaderStore =
        object :
            BookReaderStore,
            Store<BookReaderStore.Intent, BookReaderStore.State, BookReaderStore.Label> by storeFactory.create(
                name = "BookReaderStore",
                initialState = BookReaderStore.State.Initial,
                bootstrapper = SimpleBootstrapper(Action.Init),
                executorFactory = { ExecutorImpl(bookId = bookId) },
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {

        data object Init : Action
    }

    private sealed interface Msg {

        data object Loading : Msg

        data class BookLoaded(val book: BookContent) : Msg

        data object SetError : Msg

        data object Translating : Msg

        data class TranslationLoaded(val translation: BookTranslation) : Msg

        data object TranslationHidden : Msg

        data object TranslationShown : Msg

        data object TranslationFailed : Msg

        data class WordSelected(val definition: BookWordDefinition) : Msg

        data object WordDialogDismissed : Msg

        data object WordAdded : Msg

        data object WordAddedDialogDismissed : Msg
    }

    private object ReducerImpl : Reducer<BookReaderStore.State, Msg> {

        @Suppress("CyclomaticComplexMethod")
        override fun BookReaderStore.State.reduce(msg: Msg): BookReaderStore.State {
            val content = this as? BookReaderStore.State.Content
            return when (msg) {
                Msg.Loading -> BookReaderStore.State.Loading
                is Msg.BookLoaded -> BookReaderStore.State.Content(
                    book = msg.book,
                    translation = null,
                    translationVisible = false,
                    translating = false,
                    selectedWord = null,
                    showWordAddedDialog = false,
                )

                Msg.SetError -> BookReaderStore.State.Error
                Msg.Translating -> content?.copy(translating = true) ?: this
                is Msg.TranslationLoaded -> content?.copy(
                    translation = msg.translation,
                    translationVisible = true,
                    translating = false,
                ) ?: this

                Msg.TranslationHidden -> content?.copy(translationVisible = false) ?: this
                Msg.TranslationShown -> content?.copy(translationVisible = true) ?: this
                Msg.TranslationFailed -> content?.copy(translating = false) ?: this
                is Msg.WordSelected -> content?.copy(selectedWord = msg.definition) ?: this
                Msg.WordDialogDismissed -> content?.copy(
                    selectedWord = null,
                    showWordAddedDialog = false,
                ) ?: this

                Msg.WordAdded -> content?.copy(showWordAddedDialog = true) ?: this
                Msg.WordAddedDialogDismissed -> content?.copy(
                    selectedWord = null,
                    showWordAddedDialog = false,
                ) ?: this
            }
        }
    }

    private inner class ExecutorImpl(
        private val bookId: String,
    ) : BaseCoroutineExecutor<
            BookReaderStore.Intent,
            Action,
            BookReaderStore.State,
            Msg,
            BookReaderStore.Label,
            >() {

        override fun executeAction(action: Action) {
            when (action) {
                Action.Init -> loadBook()
            }
        }

        override fun executeIntent(intent: BookReaderStore.Intent) {
            when (intent) {
                BookReaderStore.Intent.Close -> publish(BookReaderStore.Label.Close)
                BookReaderStore.Intent.Retry -> loadBook()
                BookReaderStore.Intent.ToggleTranslate -> handleToggleTranslate()
                is BookReaderStore.Intent.SelectWord -> selectWord(wordId = intent.wordId)
                BookReaderStore.Intent.DismissWordDialog -> dispatch(Msg.WordDialogDismissed)
                BookReaderStore.Intent.AddWordToCard -> addWordToCard()
                BookReaderStore.Intent.DismissWordAddedDialog -> {
                    dispatch(Msg.WordAddedDialogDismissed)
                }
            }
        }

        private fun loadBook() {
            dispatch(Msg.Loading)
            scope.launch {
                try {
                    val book = getBookContentUseCase(bookId)
                    dispatch(Msg.BookLoaded(book = book))
                } catch (_: Exception) {
                    dispatch(Msg.SetError)
                }
            }
        }

        private fun handleToggleTranslate() {
            val content = state() as? BookReaderStore.State.Content ?: return
            when {
                content.translationVisible -> dispatch(Msg.TranslationHidden)
                content.translation != null -> dispatch(Msg.TranslationShown)
                content.translating -> return
                else -> {
                    dispatch(Msg.Translating)
                    scope.launch {
                        try {
                            val translation = getBookTranslationUseCase(bookId)
                            dispatch(Msg.TranslationLoaded(translation = translation))
                        } catch (_: Exception) {
                            dispatch(Msg.TranslationFailed)
                        }
                    }
                }
            }
        }

        private fun selectWord(wordId: String) {
            val content = state() as? BookReaderStore.State.Content ?: return
            val definition = findDefinition(book = content.book, wordId = wordId) ?: return
            dispatch(Msg.WordSelected(definition = definition))
        }

        private fun addWordToCard() {
            val content = state() as? BookReaderStore.State.Content ?: return
            val definition = content.selectedWord ?: return
            dispatch(Msg.WordAdded)
            publish(BookReaderStore.Label.AddWordToCard(definition = definition))
        }

        private fun findDefinition(
            book: BookContent,
            wordId: String,
        ): BookWordDefinition? {
            for (paragraph in book.paragraphs) {
                for (segment in paragraph.segments) {
                    if (segment.type == BookTextSegmentType.LOOKUP_WORD && segment.id == wordId) {
                        return segment.definition
                    }
                }
            }
            return null
        }
    }
}