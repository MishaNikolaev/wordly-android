package com.nmichail.wordly.android.features.books.detail.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.core.navigation.componentScope
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import kotlinx.coroutines.launch

internal class DefaultBookDetailComponent(
	componentContext: ComponentContext,
	bookId: String,
	initialBook: BookDetail?,
	bookDetailStoreFactory: BookDetailStoreFactory,
	private val bookDetailRouter: BookDetailRouter,
	private val onReadClick: (bookId: String) -> Unit,
	private val onSimilarBookClick: (BooksItem) -> Unit,
) : ComponentContext by componentContext,
	BookDetailComponent {

	private val store: BookDetailStore = instanceKeeper.getStore {
		bookDetailStoreFactory.create(
			bookId = bookId,
			initialBook = initialBook,
		)
	}

	override val model: Value<BookDetailStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					BookDetailStore.Label.Close -> bookDetailRouter.navigateBack()
					is BookDetailStore.Label.OpenReader -> onReadClick(label.bookId)
					is BookDetailStore.Label.OpenSimilarBook -> onSimilarBookClick(label.book)
				}
			}
		}
	}

	override fun handleBack() {
		store.accept(BookDetailStore.Intent.Back)
	}

	override fun handleRetry() {
		store.accept(BookDetailStore.Intent.Retry)
	}

	override fun handleRead() {
		store.accept(BookDetailStore.Intent.Read)
	}

	override fun handleSimilarBookClick(bookId: String) {
		store.accept(BookDetailStore.Intent.SelectSimilarBook(bookId = bookId))
	}
}