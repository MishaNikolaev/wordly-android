package com.nmichail.wordly.android.features.books.reader.domain.repository

import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation

interface BookReaderRepository {

    suspend fun getBookContent(bookId: String): BookContent

    suspend fun getBookTranslation(bookId: String): BookTranslation
}
