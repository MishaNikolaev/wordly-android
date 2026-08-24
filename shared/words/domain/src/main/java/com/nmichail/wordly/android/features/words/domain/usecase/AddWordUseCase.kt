package com.nmichail.wordly.android.features.words.domain.usecase

import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import javax.inject.Inject

class AddWordUseCase @Inject constructor(
	private val wordsRepository: WordsRepository,
) {

	suspend operator fun invoke(word: NewWord) {
		wordsRepository.addWord(word)
	}
}
