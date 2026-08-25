package com.nmichail.wordly.android.features.books.reader.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookWordDefinition
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class DefaultBookReaderComponent @AssistedInject constructor(
	private val bookReaderStoreFactory: BookReaderStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("bookId") bookId: String,
	@Assisted("bookReaderRouter") private val bookReaderRouter: BookReaderRouter,
	@Assisted("onAddWordToCard") private val onAddWordToCard: (BookWordDefinition) -> Unit,
) : ComponentContext by componentContext,
    BookReaderComponent {

    private val store: BookReaderStore = instanceKeeper.getStore {
        bookReaderStoreFactory.create(bookId = bookId)
    }

    override val model: Value<BookReaderStore.State> = store.asValue()

    init {
        componentScope().launch {
            for (label in store.labelsChannel(lifecycle)) {
                when (label) {
                    BookReaderStore.Label.Close -> bookReaderRouter.navigateBack()
                    is BookReaderStore.Label.AddWordToCard -> {
                        onAddWordToCard(label.definition)
                    }
                }
            }
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

	@AssistedFactory
	fun interface Factory : BookReaderComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("bookId") bookId: String,
			@Assisted("bookReaderRouter") bookReaderRouter: BookReaderRouter,
			@Assisted("onAddWordToCard") onAddWordToCard: (BookWordDefinition) -> Unit,
		): DefaultBookReaderComponent
	}
}