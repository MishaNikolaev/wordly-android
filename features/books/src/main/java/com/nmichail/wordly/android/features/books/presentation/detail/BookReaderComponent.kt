package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition

interface BookReaderComponent {

	val model: Value<State>

	fun handleClose()

	fun handleRetry()

	fun handleToggleTranslate()

	fun handleSelectWord(wordId: String)

	fun handleDismissWordDialog()

	fun handleAddWordToCard()

	fun handleDismissWordAddedDialog()

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val book: BookContent,
			val translation: BookTranslation?,
			val isTranslationVisible: Boolean,
			val isTranslating: Boolean,
			val selectedWord: BookWordDefinition?,
			val showWordAddedDialog: Boolean,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data class AddWordToCard(val definition: BookWordDefinition) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			bookId: String,
			bookReaderRouter: BookReaderRouter,
			onAddWordToCard: (BookWordDefinition) -> Unit,
		): BookReaderComponent
	}
}