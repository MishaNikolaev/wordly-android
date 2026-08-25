package com.nmichail.wordly.android.features.books.reader.data.repository

import com.nmichail.wordly.android.features.books.reader.data.datasource.BookReaderDataSource
import com.nmichail.wordly.android.features.books.reader.data.mapper.toEntity
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.domain.repository.BookReaderRepository
import javax.inject.Inject

class BookReaderRepositoryImpl @Inject constructor(
	private val dataSource: BookReaderDataSource,
) : BookReaderRepository {

	override suspend fun getBookContent(bookId: String): BookContent =
		dataSource.getBookContent(bookId).toEntity()

	override suspend fun getBookTranslation(bookId: String): BookTranslation =
		dataSource.getBookTranslation(bookId).toEntity()
}
