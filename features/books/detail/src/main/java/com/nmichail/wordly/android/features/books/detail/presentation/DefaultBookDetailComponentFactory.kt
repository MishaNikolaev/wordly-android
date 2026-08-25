package com.nmichail.wordly.android.features.books.detail.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import javax.inject.Inject

internal class DefaultBookDetailComponentFactory @Inject constructor(
	private val bookDetailStoreFactory: BookDetailStoreFactory,
) : BookDetailComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		bookId: String,
		initialBook: BookDetail?,
		bookDetailRouter: BookDetailRouter,
		onReadClick: (bookId: String) -> Unit,
		onSimilarBookClick: (BooksItem) -> Unit,
	): BookDetailComponent =
		DefaultBookDetailComponent(
			componentContext = componentContext,
			bookId = bookId,
			initialBook = initialBook,
			bookDetailStoreFactory = bookDetailStoreFactory,
			bookDetailRouter = bookDetailRouter,
			onReadClick = onReadClick,
			onSimilarBookClick = onSimilarBookClick,
		)
}