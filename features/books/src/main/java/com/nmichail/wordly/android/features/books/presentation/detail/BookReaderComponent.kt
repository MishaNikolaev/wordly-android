package com.nmichail.wordly.android.features.books.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition

interface BookReaderComponent {

	val model: Value<BookReaderStore.State>

	fun handleClose()

	fun handleRetry()

	fun handleToggleTranslate()

	fun handleSelectWord(wordId: String)

	fun handleDismissWordDialog()

	fun handleAddWordToCard()

	fun handleDismissWordAddedDialog()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			bookId: String,
			bookReaderRouter: BookReaderRouter,
			onAddWordToCard: (BookWordDefinition) -> Unit,
		): BookReaderComponent
	}
}