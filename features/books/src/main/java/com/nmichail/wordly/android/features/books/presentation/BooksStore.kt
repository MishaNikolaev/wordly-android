package com.nmichail.wordly.android.features.books.presentation

import com.arkivanov.mvikotlin.core.store.Store

internal interface BooksStore :
	Store<BooksStore.Intent, BooksComponent.State, BooksComponent.Label> {

	sealed interface Intent {

		data object Back : Intent

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class ChangeLevel(val level: String) : Intent

		data class SelectBook(val bookId: String) : Intent
	}
}