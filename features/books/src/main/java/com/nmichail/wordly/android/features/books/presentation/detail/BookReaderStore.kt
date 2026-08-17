package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition

interface BookReaderStore :
    Store<BookReaderStore.Intent, BookReaderStore.State, BookReaderStore.Label> {

    sealed interface State {

        data object Initial : State

        data object Loading : State

        data class Content(
            val book: BookContent,
            val translation: BookTranslation?,
            val translationVisible: Boolean,
            val translating: Boolean,
            val selectedWord: BookWordDefinition?,
            val showWordAddedDialog: Boolean,
        ) : State

        data object Error : State
    }

    sealed interface Label {

        data object Close : Label

        data class AddWordToCard(val definition: BookWordDefinition) : Label
    }

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