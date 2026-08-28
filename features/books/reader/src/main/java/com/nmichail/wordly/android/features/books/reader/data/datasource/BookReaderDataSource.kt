package com.nmichail.wordly.android.features.books.reader.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.books.reader.data.api.BookReaderApi
import com.nmichail.wordly.android.features.books.reader.data.dto.BookContentResponse
import com.nmichail.wordly.android.features.books.reader.data.dto.BookTranslationResponse
import javax.inject.Inject

interface BookReaderDataSource {

	suspend fun getBookContent(bookId: String): BookContentResponse

	suspend fun getBookTranslation(bookId: String): BookTranslationResponse
}

class BookReaderDataSourceImpl @Inject constructor(
	private val api: BookReaderApi,
	private val cache: JsonCacheStore,
) : BookReaderDataSource {

	override suspend fun getBookContent(bookId: String): BookContentResponse =
		cache.getOrFetch(
			key = contentKey(bookId),
			type = BookContentResponse::class.java,
		) {
			api.getBookContent(bookId)
		}

	override suspend fun getBookTranslation(bookId: String): BookTranslationResponse =
		cache.getOrFetch(
			key = translationKey(bookId),
			type = BookTranslationResponse::class.java,
		) {
			api.getBookTranslation(bookId)
		}

	private companion object {
		const val CACHE_VERSION = 10

		fun contentKey(bookId: String): String = "page_book_content_v${CACHE_VERSION}_$bookId"

		fun translationKey(bookId: String): String = "page_book_translation_v${CACHE_VERSION}_$bookId"
	}
}