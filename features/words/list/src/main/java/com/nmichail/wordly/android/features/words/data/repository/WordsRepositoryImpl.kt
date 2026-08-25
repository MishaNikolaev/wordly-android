@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.features.words.data.repository

import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.features.words.data.api.FreeDictionaryApi
import com.nmichail.wordly.android.features.words.data.api.MyMemoryApi
import com.nmichail.wordly.android.features.words.data.api.WordsApi
import com.nmichail.wordly.android.features.words.data.datasource.WordsDataSource
import com.nmichail.wordly.android.features.words.data.dto.AddToReviewBody
import com.nmichail.wordly.android.features.words.data.dto.UpdateWordStatusBody
import com.nmichail.wordly.android.features.words.data.dto.VocabularyLookupDto
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Singleton
class WordsRepositoryImpl @Inject constructor(
	private val freeDictionaryApi: FreeDictionaryApi,
	private val myMemoryApi: MyMemoryApi,
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
		return coroutineScope {
			val dictionaryDeferred = async {
				try {
					freeDictionaryApi.lookup(word = normalized).toWordLookup(query = normalized)
				} catch (_: Exception) {
					null
				}
			}
			val vocabularyDeferred = async {
				try {
					wordsApi.lookupVocabulary(query = normalized).toWordLookup()
				} catch (_: Exception) {
					null
				}
			}
			val merged = mergeLookups(
				query = normalized,
				dictionary = dictionaryDeferred.await(),
				vocabulary = vocabularyDeferred.await(),
			)
			val withTranslation = when {
				merged == null -> {
					val translation = translateEnToRu(normalized)
						?: error("Word not found: $normalized")
					WordLookup(
						word = normalized,
						phonetic = null,
						translation = translation,
						definition = null,
						examples = emptyList(),
						difficulty = 2,
					)
				}
				!merged.translation.isNullOrBlank() -> merged
				else -> merged.copy(translation = translateEnToRu(normalized))
			}
			withTranslation
		}
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

	private suspend fun translateEnToRu(text: String): String? =
		try {
			val translated = myMemoryApi.translate(query = text.take(450))
				.responseData
				?.translatedText
				?.trim()
				?.takeIf { it.isNotEmpty() }
				?: return null
			when {
				MYMEMORY_WARNING.containsMatchIn(translated) -> null
				translated.equals(text, ignoreCase = true) -> null
				else -> translated
			}
		} catch (_: Exception) {
			null
		}

	private fun mockLookup(query: String): WordLookup =
		WordLookup(
			word = query,
			phonetic = "/rɪˈzɪliəns/",
			translation = "устойчивость, стойкость",
			definition = "the capacity to recover quickly from difficulties; toughness; " +
				"the ability to spring back into shape; mental toughness under pressure",
			examples = listOf(
				WordExample(
					text = "Mental resilience matters.",
					translation = "Психическая стойкость важна.",
				),
				WordExample(
					text = "She showed great resilience.",
					translation = "Она проявила это в полной мере.",
				),
				WordExample(
					text = "Resilience is built over time.",
					translation = "Устойчивость формируется со временем.",
				),
			),
			difficulty = 2,
		)

	private companion object {
		val MYMEMORY_WARNING = Regex("mymemory warning", RegexOption.IGNORE_CASE)
	}
}

private fun VocabularyLookupDto.toWordLookup(): WordLookup =
	WordLookup(
		word = word,
		phonetic = phonetic,
		translation = translation,
		definition = definition,
		examples = examples.orEmpty().map { example ->
			WordExample(text = example.text, translation = example.translation)
		},
		difficulty = difficulty ?: 2,
	)

private fun mergeLookups(
	query: String,
	dictionary: WordLookup?,
	vocabulary: WordLookup?,
): WordLookup? {
	if (dictionary == null && vocabulary == null) return null
	val vocabularyExamples = vocabulary?.examples.orEmpty()
	val dictionaryExamples = dictionary?.examples.orEmpty()
	val examples = when {
		vocabularyExamples.any { !it.translation.isNullOrBlank() } -> vocabularyExamples
		dictionaryExamples.isNotEmpty() -> dictionaryExamples
		else -> vocabularyExamples
	}
	return WordLookup(
		word = dictionary?.word?.takeIf { it.isNotBlank() }
			?: vocabulary?.word?.takeIf { it.isNotBlank() }
			?: query,
		phonetic = dictionary?.phonetic ?: vocabulary?.phonetic,
		translation = vocabulary?.translation,
		definition = dictionary?.definition ?: vocabulary?.definition,
		examples = examples,
		difficulty = vocabulary?.difficulty ?: dictionary?.difficulty ?: 2,
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