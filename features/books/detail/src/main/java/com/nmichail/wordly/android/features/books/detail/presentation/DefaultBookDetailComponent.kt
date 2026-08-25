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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class DefaultBookDetailComponent @AssistedInject constructor(
	private val bookDetailStoreFactory: BookDetailStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("bookId") bookId: String,
	@Assisted("initialBook") initialBook: BookDetail?,
	@Assisted("bookDetailRouter") private val bookDetailRouter: BookDetailRouter,
	@Assisted("onReadClick") private val onReadClick: (bookId: String) -> Unit,
	@Assisted("onSimilarBookClick") private val onSimilarBookClick: (BooksItem) -> Unit,
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

	@AssistedFactory
	fun interface Factory : BookDetailComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("bookId") bookId: String,
			@Assisted("initialBook") initialBook: BookDetail?,
			@Assisted("bookDetailRouter") bookDetailRouter: BookDetailRouter,
			@Assisted("onReadClick") onReadClick: (bookId: String) -> Unit,
			@Assisted("onSimilarBookClick") onSimilarBookClick: (BooksItem) -> Unit,
		): DefaultBookDetailComponent
	}
}