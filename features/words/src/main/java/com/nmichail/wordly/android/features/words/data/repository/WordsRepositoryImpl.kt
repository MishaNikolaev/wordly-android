@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.features.words.data.repository

import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.features.words.data.api.FreeDictionaryApi
import com.nmichail.wordly.android.features.words.data.api.WordsApi
import com.nmichail.wordly.android.features.words.data.dto.AddToReviewBody
import com.nmichail.wordly.android.features.words.data.dto.UpdateWordStatusBody
import com.nmichail.wordly.android.features.words.data.mapper.toWordLookup
import com.nmichail.wordly.android.features.words.data.mapper.toBody
import com.nmichail.wordly.android.features.words.data.mapper.toDomain
import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordLookup
import com.nmichail.wordly.android.features.words.domain.entity.WordReview
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.entity.WordTag
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordsRepositoryImpl @Inject constructor(
	private val freeDictionaryApi: FreeDictionaryApi,
	private val wordsApi: WordsApi,
	private val mockDataSource: MockDataSource,
) : WordsRepository {
	override suspend fun getWords() = wordsApi.getWords().toDomain()

	override suspend fun getTags(): List<WordTag> = getWords().tags

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
	}

	override suspend fun updateWordStatus(wordId: String, status: WordStatus) {
		wordsApi.updateStatus(
			wordId = wordId,
			body = UpdateWordStatusBody(status = status.toApiStatus()),
		)
	}

	override suspend fun addToReview(review: WordReview) {
		wordsApi.addToReview(
			wordId = review.wordId,
			body = AddToReviewBody(epochDay = review.epochDay),
		)
	}

	private fun mockLookup(query: String): WordLookup =
		WordLookup(
			word = query,
			phonetic = "/rɪˈzɪliəns/",
			// TODO: Free Dictionary API не отдает перевод на русский.
			// Нужен отдельный источник перевода, после этого заполню поле translation.
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
private fun WordStatus.toApiStatus(): String =
	when (this) {
		WordStatus.New -> "NEW"
		WordStatus.InProgress -> "IN_PROGRESS"
		WordStatus.Learned -> "LEARNED"
	}