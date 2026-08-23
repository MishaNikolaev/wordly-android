@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.features.words.data.repository

import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.features.words.data.api.FreeDictionaryApi
import com.nmichail.wordly.android.features.words.data.api.WordsApi
import com.nmichail.wordly.android.features.words.data.datasource.WordsDataSource
import com.nmichail.wordly.android.features.words.data.dto.AddToReviewBody
import com.nmichail.wordly.android.features.words.data.dto.UpdateWordStatusBody
import com.nmichail.wordly.android.features.words.data.mapper.toBody
import com.nmichail.wordly.android.features.words.data.mapper.toDomain
import com.nmichail.wordly.android.features.words.data.mapper.toWordLookup
import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordLookup
import com.nmichail.wordly.android.features.words.domain.entity.WordReview
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.entity.WordTag
import com.nmichail.wordly.android.features.words.domain.entity.WordsCatalog
import com.nmichail.wordly.android.features.words.domain.entity.WordsFilters
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordsRepositoryImpl @Inject constructor(
	private val freeDictionaryApi: FreeDictionaryApi,
	private val wordsApi: WordsApi,
	private val wordsDataSource: WordsDataSource,
	private val mockDataSource: MockDataSource,
) : WordsRepository {

	override suspend fun getWords(filters: WordsFilters): WordsCatalog {
		val status = filters.filter.toApiStatus()
		val query = filters.query.trim().takeIf { it.isNotEmpty() }
		return wordsDataSource.getWords(status = status, query = query).toDomain()
	}

	override suspend fun getTags(): List<WordTag> =
		getWords(
			filters = WordsFilters(
				filter = WordFilter.All,
				query = "",
			),
		).tags

	override suspend fun lookupWord(query: String): WordLookup {
		val normalized = query.trim().lowercase()
		require(normalized.isNotEmpty())
		if (mockDataSource.isMock()) {
			return mockLookup(query = normalized)
		}
		return freeDictionaryApi.lookup(word = normalized).toWordLookup(query = normalized)
	}

	override suspend fun addWord(word: NewWord) {
		wordsApi.addWord(body = word.toBody())
		wordsDataSource.invalidateCache()
	}

	override suspend fun updateWordStatus(wordId: String, status: WordStatus) {
		wordsApi.updateStatus(
			wordId = wordId,
			body = UpdateWordStatusBody(status = status.toApiStatus()),
		)
		wordsDataSource.invalidateCache()
	}

	override suspend fun addToReview(review: WordReview) {
		wordsApi.addToReview(
			wordId = review.wordId,
			body = AddToReviewBody(epochDay = review.epochDay),
		)
		wordsDataSource.invalidateCache()
	}

	private fun mockLookup(query: String): WordLookup =
		WordLookup(
			word = query,
			phonetic = "/rɪˈzɪliəns/",
			translation = null,
			definition = "the capacity to recover quickly from difficulties; toughness",
			examples = listOf(
				WordExample(
					text = "Mental resilience matters.",
					translation = "Психическая стойкость важна.",
				),
				WordExample(
					text = "She showed great resilience.",
					translation = "Она проявила это в полной мере.",
				),
			),
			difficulty = 2,
		)
}

private fun WordFilter.toApiStatus(): String? =
	when (this) {
		WordFilter.All -> null
		WordFilter.New -> "NEW"
		WordFilter.InProgress -> "IN_PROGRESS"
		WordFilter.Learned -> "LEARNED"
	}

private fun WordStatus.toApiStatus(): String =
	when (this) {
		WordStatus.New -> "NEW"
		WordStatus.InProgress -> "IN_PROGRESS"
		WordStatus.Learned -> "LEARNED"
	}
