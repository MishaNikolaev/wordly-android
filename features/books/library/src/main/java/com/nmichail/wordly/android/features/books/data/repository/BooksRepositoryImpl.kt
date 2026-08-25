package com.nmichail.wordly.android.features.books.data.repository

import com.nmichail.wordly.android.features.books.data.datasource.BooksDataSource
import com.nmichail.wordly.android.features.books.data.mapper.toEntity
import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.repository.BooksRepository
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
	private val dataSource: BooksDataSource,
) : BooksRepository {

	override suspend fun getCatalog(): BooksCatalog =
		dataSource.getCatalog().toEntity()
}
