package com.nmichail.wordly.android.features.words.domain.usecase

import com.nmichail.wordly.android.features.words.domain.entity.WordReview
import com.nmichail.wordly.android.features.words.domain.repository.WordsRepository
import javax.inject.Inject

class AddWordToReviewUseCase @Inject constructor(
	private val wordsRepository: WordsRepository,
) {

	suspend operator fun invoke(review: WordReview) {
		wordsRepository.addToReview(review)
	}
}