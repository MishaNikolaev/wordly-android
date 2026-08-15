package com.nmichail.wordly.android.features.words.domain.usecase

import com.nmichail.wordly.android.features.words.domain.entity.WordLookup
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import javax.inject.Inject

class LookupWordUseCase @Inject constructor(
	private val wordsRepository: WordsRepository,
) {

	suspend operator fun invoke(query: String): WordLookup = wordsRepository.lookupWord(query)
}