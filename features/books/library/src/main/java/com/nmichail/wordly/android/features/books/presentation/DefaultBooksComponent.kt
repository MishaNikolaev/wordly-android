package com.nmichail.wordly.android.features.books.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class DefaultBooksComponent @AssistedInject constructor(
	private val booksStoreFactory: BooksStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("booksRouter") private val booksRouter: BooksRouter,
	@Assisted("onBookClick") private val onBookClick: (BooksItem) -> Unit,
) : ComponentContext by componentContext,
	BooksComponent {

	private val store: BooksStore = instanceKeeper.getStore {
		booksStoreFactory.create()
	}

	override val model: Value<BooksStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					BooksStore.Label.Close -> booksRouter.navigateBack()
					is BooksStore.Label.OpenBook -> onBookClick(label.book)
				}
			}
		}
	}

	override fun handleBack() {
		store.accept(BooksStore.Intent.Back)
	}

	override fun handleRetry() {
		store.accept(BooksStore.Intent.Retry)
	}

	override fun handleSearchQueryChange(query: String) {
		store.accept(BooksStore.Intent.ChangeSearchQuery(query = query))
	}

	override fun handleLevelChange(level: String) {
		store.accept(BooksStore.Intent.ChangeLevel(level = level))
	}

	override fun handleBookClick(bookId: String) {
		store.accept(BooksStore.Intent.SelectBook(bookId = bookId))
	}

	@AssistedFactory
	fun interface Factory : BooksComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("booksRouter") booksRouter: BooksRouter,
			@Assisted("onBookClick") onBookClick: (BooksItem) -> Unit,
		): DefaultBooksComponent
	}
}