package com.nmichail.wordly.android.features.books.reader.data.repository

import com.nmichail.wordly.android.features.books.reader.data.api.BookReaderApi
import com.nmichail.wordly.android.features.books.reader.data.mapper.toEntity
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.domain.repository.BookReaderRepository
import javax.inject.Inject

class BookReaderRepositoryImpl @Inject constructor(
    private val bookReaderApi: BookReaderApi,
) : BookReaderRepository {

    override suspend fun getBookContent(bookId: String): BookContent =
        bookReaderApi.getBookContent(bookId).toEntity()

    override suspend fun getBookTranslation(bookId: String): BookTranslation =
        bookReaderApi.getBookTranslation(bookId).toEntity()
}
