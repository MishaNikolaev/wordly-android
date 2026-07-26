package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition

internal class DefaultBookReaderComponent(
	componentContext: ComponentContext,
	bookId: String,
	bookReaderStoreFactory: BookReaderStoreFactory,
	private val bookReaderRouter: BookReaderRouter,
	private val onAddWordToCard: (BookWordDefinition) -> Unit,
) : ComponentContext by componentContext,
	BookReaderComponent {

	private val store: BookReaderStore = instanceKeeper.getStore {
		bookReaderStoreFactory.create(bookId = bookId)
	}

	override val model: Value<BookReaderComponent.State> = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					BookReaderComponent.Label.Close -> bookReaderRouter.navigateBack()
					is BookReaderComponent.Label.AddWordToCard -> {
						onAddWordToCard(label.definition)
					}
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleClose() {
		store.accept(BookReaderStore.Intent.Close)
	}

	override fun handleRetry() {
		store.accept(BookReaderStore.Intent.Retry)
	}

	override fun handleToggleTranslate() {
		store.accept(BookReaderStore.Intent.ToggleTranslate)
	}

	override fun handleSelectWord(wordId: String) {
		store.accept(BookReaderStore.Intent.SelectWord(wordId = wordId))
	}

	override fun handleDismissWordDialog() {
		store.accept(BookReaderStore.Intent.DismissWordDialog)
	}

	override fun handleAddWordToCard() {
		store.accept(BookReaderStore.Intent.AddWordToCard)
	}

	override fun handleDismissWordAddedDialog() {
		store.accept(BookReaderStore.Intent.DismissWordAddedDialog)
	}
}