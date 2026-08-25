package com.nmichail.wordly.android.features.books.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import javax.inject.Inject

internal class DefaultBooksComponentFactory @Inject constructor(
	private val booksStoreFactory: BooksStoreFactory,
) : BooksComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		booksRouter: BooksRouter,
		onBookClick: (BooksItem) -> Unit,
	): BooksComponent =
		DefaultBooksComponent(
			componentContext = componentContext,
			booksStoreFactory = booksStoreFactory,
			booksRouter = booksRouter,
			onBookClick = onBookClick,
		)
}