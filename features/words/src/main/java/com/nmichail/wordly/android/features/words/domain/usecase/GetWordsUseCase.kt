package com.nmichail.wordly.android.features.words.domain.usecase

import com.nmichail.wordly.android.features.words.domain.entity.WordsCatalog
import com.nmichail.wordly.android.features.words.domain.entity.WordsFilters
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import javax.inject.Inject

class GetWordsUseCase @Inject constructor(
	private val wordsRepository: WordsRepository,
) {

	suspend operator fun invoke(filters: WordsFilters): WordsCatalog =
		wordsRepository.getWords(filters = filters)
}