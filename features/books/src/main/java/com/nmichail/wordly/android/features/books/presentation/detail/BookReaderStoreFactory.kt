package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.domain.usecase.GetBookContentUseCase
import com.nmichail.wordly.android.features.books.domain.usecase.GetBookTranslationUseCase
import javax.inject.Inject
import kotlinx.coroutines.delay

private const val TRANSLATE_DEMO_DELAY_MS = 800L

internal class BookReaderStoreFactory @Inject constructor(
	private val getBookContentUseCase: GetBookContentUseCase,
	private val getBookTranslationUseCase: GetBookTranslationUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(bookId: String): BookReaderStore =
		object :
			BookReaderStore,
			Store<
				BookReaderStore.Intent,
				BookReaderComponent.State,
				BookReaderComponent.Label,
				> by storeFactory.create(
				name = "BookReaderStore",
				initialState = BookReaderComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = { ExecutorImpl(bookId = bookId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
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

	private object ReducerImpl : Reducer<BookReaderComponent.State, Msg> {

		override fun BookReaderComponent.State.reduce(
			msg: Msg,
		): BookReaderComponent.State =
			when (msg) {
				Msg.Loading -> BookReaderComponent.State.Loading
				is Msg.BookLoaded -> BookReaderComponent.State.Content(
					book = msg.book,
					translation = null,
					isTranslationVisible = false,
					isTranslating = false,
					selectedWord = null,
					showWordAddedDialog = false,
				)
				Msg.SetError -> BookReaderComponent.State.Error
				else -> reduceContent(msg)
			}

		private fun BookReaderComponent.State.reduceContent(
			msg: Msg,
		): BookReaderComponent.State {
			val content = this as? BookReaderComponent.State.Content ?: return this
			return when (msg) {
				Msg.Translating -> content.copy(isTranslating = true)
				is Msg.TranslationLoaded -> content.copy(
					translation = msg.translation,
					isTranslationVisible = true,
					isTranslating = false,
				)
				Msg.TranslationHidden -> content.copy(isTranslationVisible = false)
				Msg.TranslationShown -> content.copy(isTranslationVisible = true)
				Msg.TranslationFailed -> content.copy(isTranslating = false)
				is Msg.WordSelected -> content.copy(selectedWord = msg.definition)
				Msg.WordDialogDismissed -> content.copy(
					selectedWord = null,
					showWordAddedDialog = false,
				)
				Msg.WordAdded -> content.copy(showWordAddedDialog = true)
				Msg.WordAddedDialogDismissed -> content.copy(
					selectedWord = null,
					showWordAddedDialog = false,
				)
				else -> this
			}
		}
	}

	private inner class ExecutorImpl(
		private val bookId: String,
	) : BaseCoroutineExecutor<
		BookReaderStore.Intent,
		Action,
		BookReaderComponent.State,
		Msg,
		BookReaderComponent.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadBook()
			}
		}

		override fun executeIntent(intent: BookReaderStore.Intent) {
			when (intent) {
				BookReaderStore.Intent.Close -> publish(BookReaderComponent.Label.Close)
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
			launchTry {
				val book = getBookContentUseCase(bookId)
				dispatch(Msg.BookLoaded(book = book))
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun handleToggleTranslate() {
			val content = state() as? BookReaderComponent.State.Content ?: return
			when {
				content.isTranslationVisible -> dispatch(Msg.TranslationHidden)
				content.translation != null -> dispatch(Msg.TranslationShown)
				content.isTranslating -> return
				else -> {
					dispatch(Msg.Translating)
					launchTry {
						delay(TRANSLATE_DEMO_DELAY_MS)
						val translation = getBookTranslationUseCase(bookId)
						dispatch(Msg.TranslationLoaded(translation = translation))
					} catch {
						dispatch(Msg.TranslationFailed)
					}
				}
			}
		}

		private fun selectWord(wordId: String) {
			val content = state() as? BookReaderComponent.State.Content ?: return
			val definition = findDefinition(book = content.book, wordId = wordId) ?: return
			dispatch(Msg.WordSelected(definition = definition))
		}

		private fun addWordToCard() {
			val content = state() as? BookReaderComponent.State.Content ?: return
			val definition = content.selectedWord ?: return
			dispatch(Msg.WordAdded)
			publish(BookReaderComponent.Label.AddWordToCard(definition = definition))
		}

		private fun findDefinition(
			book: BookContent,
			wordId: String,
		): BookWordDefinition? =
			book.paragraphs
				.asSequence()
				.flatMap { it.segments.asSequence() }
				.firstOrNull { segment ->
					segment.type == BookTextSegmentType.LOOKUP_WORD && segment.id == wordId
				}
				?.definition
	}
}