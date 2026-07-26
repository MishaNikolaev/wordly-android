package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store

internal interface BookReaderStore :
	Store<BookReaderStore.Intent, BookReaderComponent.State, BookReaderComponent.Label> {

	sealed interface Intent {

		data object Close : Intent

		data object Retry : Intent

		data object ToggleTranslate : Intent

		data class SelectWord(val wordId: String) : Intent

		data object DismissWordDialog : Intent

		data object AddWordToCard : Intent

		data object DismissWordAddedDialog : Intent
	}
}