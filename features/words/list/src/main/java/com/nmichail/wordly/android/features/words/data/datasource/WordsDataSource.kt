package com.nmichail.wordly.android.features.words.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.words.data.api.WordsApi
import com.nmichail.wordly.android.features.words.data.dto.WordsCatalogDto
import javax.inject.Inject

interface WordsDataSource {

	suspend fun getWords(
		status: String?,
		query: String?,
	): WordsCatalogDto

	suspend fun invalidateCache()
}

class WordsDataSourceImpl @Inject constructor(
	private val api: WordsApi,
	private val cache: JsonCacheStore,
) : WordsDataSource {

	override suspend fun getWords(
		status: String?,
		query: String?,
	): WordsCatalogDto {
		// Cache only unfiltered/status lists; search always hits network.
		if (query != null) {
			return api.getWords(status = status, query = query)
		}
		return cache.getOrFetch(
			key = wordsKey(status ?: "all"),
			type = WordsCatalogDto::class.java,
		) {
			api.getWords(status = status, query = null)
		}
	}

	override suspend fun invalidateCache() {
		cache.clear(key = wordsKey("all"))
		cache.clear(key = wordsKey("NEW"))
		cache.clear(key = wordsKey("IN_PROGRESS"))
		cache.clear(key = wordsKey("LEARNED"))
	}

	private companion object {
		fun wordsKey(status: String): String = "page_words_$status"
	}
}
