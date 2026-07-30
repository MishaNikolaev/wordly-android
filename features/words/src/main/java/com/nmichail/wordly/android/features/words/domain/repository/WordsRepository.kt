package com.nmichail.wordly.android.features.words.domain.repository

import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.entity.WordLookup
import com.nmichail.wordly.android.features.words.domain.entity.WordReview
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.entity.WordTag
import com.nmichail.wordly.android.features.words.domain.entity.WordsCatalog
import com.nmichail.wordly.android.features.words.domain.entity.WordsFilters

interface WordsRepository {

	suspend fun getWords(filters: WordsFilters): WordsCatalog

	suspend fun getTags(): List<WordTag>

	suspend fun lookupWord(query: String): WordLookup

	suspend fun addWord(word: NewWord)

	suspend fun updateWordStatus(wordId: String, status: WordStatus)

	suspend fun addToReview(review: WordReview)
}
