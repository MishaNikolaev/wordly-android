package com.nmichail.wordly.android.features.books.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.features.books.domain.entity.BooksLevelBanner
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection

interface BooksComponent {

	val model: Value<State>

	fun handleBack()

	fun handleRetry()

	fun handleSearchQueryChange(query: String)

	fun handleLevelChange(level: String)

	fun handleBookClick(bookId: String)

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val title: String,
			val searchQuery: String,
			val searchPlaceholder: String,
			val levelBanner: BooksLevelBanner?,
			val sections: List<BooksSection>,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data class OpenBook(val book: BooksItem) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			booksRouter: BooksRouter,
			onBookClick: (BooksItem) -> Unit,
		): BooksComponent
	}
}