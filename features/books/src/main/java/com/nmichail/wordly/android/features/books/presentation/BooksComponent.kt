package com.nmichail.wordly.android.features.books.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem

interface BooksComponent {

	val model: Value<BooksStore.State>

	fun handleBack()

	fun handleRetry()

	fun handleSearchQueryChange(query: String)

	fun handleLevelChange(level: String)

	fun handleBookClick(bookId: String)

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			booksRouter: BooksRouter,
			onBookClick: (BooksItem) -> Unit,
		): BooksComponent
	}
}