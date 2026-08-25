package com.nmichail.wordly.android.features.books.detail.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem

interface BookDetailStore :
	Store<BookDetailStore.Intent, BookDetailStore.State, BookDetailStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Loading : State

		data class Content(
			val book: BookDetail,
			val similarBooks: List<BooksItem> = emptyList(),
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object Close : Label

		data class OpenReader(
			val bookId: String,
		) : Label

		data class OpenSimilarBook(
			val book: BooksItem,
		) : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data object Back : Intent

		data object Read : Intent

		data class SelectSimilarBook(val bookId: String) : Intent
	}
}