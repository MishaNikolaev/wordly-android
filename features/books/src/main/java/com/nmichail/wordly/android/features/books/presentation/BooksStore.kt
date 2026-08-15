package com.nmichail.wordly.android.features.books.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.features.books.domain.entity.BooksLevelBanner
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection

interface BooksStore :
	Store<BooksStore.Intent, BooksStore.State, BooksStore.Label> {

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val title: String,
			val searchQuery: String,
			val searchPlaceholder: String,
			val levelBanner: BooksLevelBanner?,
			val allSections: List<BooksSection>,
			val sections: List<BooksSection>,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data class OpenBook(val book: BooksItem) : Label
	}

	sealed interface Intent {

		data object Back : Intent

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class ChangeLevel(val level: String) : Intent

		data class SelectBook(val bookId: String) : Intent
	}
}
