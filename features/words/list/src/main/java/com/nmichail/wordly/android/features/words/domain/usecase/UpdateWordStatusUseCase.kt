package com.nmichail.wordly.android.features.words.domain.usecase

import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import javax.inject.Inject

class UpdateWordStatusUseCase @Inject constructor(
	private val wordsRepository: WordsRepository,
) {

	suspend operator fun invoke(wordId: String, status: WordStatus) {
		wordsRepository.updateWordStatus(wordId = wordId, status = status)
	}
}