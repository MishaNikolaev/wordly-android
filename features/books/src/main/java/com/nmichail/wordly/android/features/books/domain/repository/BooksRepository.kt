package com.nmichail.wordly.android.features.books.domain.repository

import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog

interface BooksRepository {

	suspend fun getCatalog(): BooksCatalog

	suspend fun getBookContent(bookId: String): BookContent

	suspend fun getBookTranslation(bookId: String): BookTranslation
}