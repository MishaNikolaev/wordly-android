package com.nmichail.wordly.android.features.books.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.books.data.api.BooksApi
import com.nmichail.wordly.android.features.books.data.dto.BooksCatalogResponse
import javax.inject.Inject

interface BooksDataSource {

	suspend fun getCatalog(): BooksCatalogResponse
}

class BooksDataSourceImpl @Inject constructor(
	private val api: BooksApi,
	private val cache: JsonCacheStore,
) : BooksDataSource {

	override suspend fun getCatalog(): BooksCatalogResponse =
		cache.getOrFetch(
			key = KEY,
			type = BooksCatalogResponse::class.java,
		) {
			api.getCatalog()
		}

	private companion object {
		const val KEY = "page_books"
	}
}
