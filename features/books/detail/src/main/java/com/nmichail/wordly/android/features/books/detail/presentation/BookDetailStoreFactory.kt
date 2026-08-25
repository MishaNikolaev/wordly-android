package com.nmichail.wordly.android.features.books.detail.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.detail.domain.usecase.GetBookDetailUseCase
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.features.books.domain.usecase.GetBooksCatalogUseCase
import com.nmichail.wordly.android.shared.catalog.filterSimilarCatalogItems
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class BookDetailStoreFactory @Inject constructor(
	private val getBookDetailUseCase: GetBookDetailUseCase,
	private val getBooksCatalogUseCase: GetBooksCatalogUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(
		bookId: String,
		initialBook: BookDetail? = null,
	): BookDetailStore =
		object :
			BookDetailStore,
			Store<
				BookDetailStore.Intent,
				BookDetailStore.State,
				BookDetailStore.Label,
				> by storeFactory.create(
				name = "BookDetailStore",
				initialState = if (initialBook != null) {
					BookDetailStore.State.Content(book = initialBook)
				} else {
					BookDetailStore.State.Initial
				},
				bootstrapper = SimpleBootstrapper(
					Action.Init(bookId = bookId, hasInitial = initialBook != null),
				),
				executorFactory = { ExecutorImpl(bookId = bookId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Init(
			val bookId: String,
			val hasInitial: Boolean,
		) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data object SetError : Msg

		data class Loaded(
			val book: BookDetail,
			val similarBooks: List<BooksItem>,
		) : Msg

		data class SimilarLoaded(
			val similarBooks: List<BooksItem>,
		) : Msg
	}

	private object ReducerImpl : Reducer<BookDetailStore.State, Msg> {

		override fun BookDetailStore.State.reduce(msg: Msg): BookDetailStore.State =
			when (msg) {
				Msg.Loading -> BookDetailStore.State.Loading
				Msg.SetError -> BookDetailStore.State.Error
				is Msg.Loaded -> BookDetailStore.State.Content(
					book = msg.book,
					similarBooks = msg.similarBooks,
				)
				is Msg.SimilarLoaded -> {
					val content = this as? BookDetailStore.State.Content ?: return this
					content.copy(similarBooks = msg.similarBooks)
				}
			}
	}

	private inner class ExecutorImpl(
		private val bookId: String,
	) : BaseCoroutineExecutor<
		BookDetailStore.Intent,
		Action,
		BookDetailStore.State,
		Msg,
		BookDetailStore.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				is Action.Init -> {
					if (!action.hasInitial) {
						load(bookId = action.bookId, showLoading = true)
					} else {
						refreshInBackground(bookId = action.bookId)
						loadSimilar(book = (state() as? BookDetailStore.State.Content)?.book)
					}
				}
			}
		}

		override fun executeIntent(intent: BookDetailStore.Intent) {
			when (intent) {
				BookDetailStore.Intent.Retry -> load(bookId = bookId, showLoading = true)
				BookDetailStore.Intent.Back -> publish(BookDetailStore.Label.Close)
				BookDetailStore.Intent.Read -> {
					val content = state() as? BookDetailStore.State.Content
					publish(BookDetailStore.Label.OpenReader(bookId = content?.book?.id ?: bookId))
				}
				is BookDetailStore.Intent.SelectSimilarBook -> {
					val content = state() as? BookDetailStore.State.Content ?: return
					val book = content.similarBooks.firstOrNull { it.id == intent.bookId } ?: return
					publish(BookDetailStore.Label.OpenSimilarBook(book = book))
				}
			}
		}

		private fun refreshInBackground(bookId: String) {
			scope.launch {
				val book = try {
					getBookDetailUseCase(bookId)
				} catch (_: Exception) {
					return@launch
				}
				val current = (state() as? BookDetailStore.State.Content)?.book
				val merged = book.copy(
					title = book.title.ifBlank { current?.title.orEmpty() },
					author = book.author.ifBlank { current?.author.orEmpty() },
					coverUrl = book.coverUrl ?: current?.coverUrl,
					description = book.description.ifBlank {
						current?.description.orEmpty()
					},
					genre = book.genre?.takeIf { it.isNotBlank() } ?: current?.genre,
					category = book.category?.takeIf { it.isNotBlank() }
						?: current?.category,
					level = book.level?.takeIf { it.isNotBlank() } ?: current?.level,
				)
				val similar = loadSimilarBooks(merged)
				dispatch(Msg.Loaded(book = merged, similarBooks = similar))
			}
		}

		private fun load(
			bookId: String,
			showLoading: Boolean,
		) {
			if (showLoading) {
				dispatch(Msg.Loading)
			}
			scope.launch {
				try {
					val bookDeferred = async { getBookDetailUseCase(bookId) }
					val catalogDeferred = async {
						try {
							getBooksCatalogUseCase()
						} catch (_: Exception) {
							null
						}
					}
					val book = bookDeferred.await()
					val catalogItems = catalogDeferred.await()
						?.sections
						?.flatMap { it.items }
						.orEmpty()
					val similar = filterSimilarBooks(
						book = book,
						catalogItems = catalogItems,
					)
					dispatch(Msg.Loaded(book = book, similarBooks = similar))
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun loadSimilar(book: BookDetail?) {
			if (book == null) return
			scope.launch {
				val similar = loadSimilarBooks(book)
				dispatch(Msg.SimilarLoaded(similarBooks = similar))
			}
		}

		private suspend fun loadSimilarBooks(book: BookDetail): List<BooksItem> =
			try {
				val catalogItems = getBooksCatalogUseCase().sections.flatMap { it.items }
				filterSimilarBooks(book = book, catalogItems = catalogItems)
			} catch (_: Exception) {
				emptyList()
			}
	}
}

private fun filterSimilarBooks(
	book: BookDetail,
	catalogItems: List<BooksItem>,
): List<BooksItem> =
	filterSimilarCatalogItems(
		items = catalogItems,
		excludeItemId = book.id,
		genre = book.genre,
		getId = BooksItem::id,
		getGenre = BooksItem::genre,
	)