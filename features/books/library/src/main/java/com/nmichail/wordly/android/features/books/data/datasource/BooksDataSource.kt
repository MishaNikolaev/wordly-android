package com.nmichail.wordly.android.features.books.data.datasource

import com.nmichail.wordly.android.features.books.data.api.BooksApi
import com.nmichail.wordly.android.features.books.data.dto.BooksCatalogResponse
import javax.inject.Inject

interface BooksDataSource {

	suspend fun getCatalog(): BooksCatalogResponse
}

class BooksDataSourceImpl @Inject constructor(
	private val api: BooksApi,
) : BooksDataSource {

	override suspend fun getCatalog(): BooksCatalogResponse = api.getCatalog()
}
