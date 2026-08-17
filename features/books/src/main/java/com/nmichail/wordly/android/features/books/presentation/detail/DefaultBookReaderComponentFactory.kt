package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition
import javax.inject.Inject

internal class DefaultBookReaderComponentFactory @Inject constructor(
    private val bookReaderStoreFactory: BookReaderStoreFactory,
) : BookReaderComponent.Factory {

    override fun invoke(
        componentContext: ComponentContext,
        bookId: String,
        bookReaderRouter: BookReaderRouter,
        onAddWordToCard: (BookWordDefinition) -> Unit,
    ): BookReaderComponent =
        DefaultBookReaderComponent(
            componentContext = componentContext,
            bookId = bookId,
            bookReaderStoreFactory = bookReaderStoreFactory,
            bookReaderRouter = bookReaderRouter,
            onAddWordToCard = onAddWordToCard,
        )
}