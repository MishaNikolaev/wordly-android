package com.nmichail.wordly.android.features.books.detail.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem

interface BookDetailComponent {

	val model: Value<BookDetailStore.State>

	fun handleBack()

	fun handleRetry()

	fun handleRead()

	fun handleSimilarBookClick(bookId: String)

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			bookId: String,
			initialBook: BookDetail?,
			bookDetailRouter: BookDetailRouter,
			onReadClick: (bookId: String) -> Unit,
			onSimilarBookClick: (BooksItem) -> Unit,
		): BookDetailComponent
	}
}