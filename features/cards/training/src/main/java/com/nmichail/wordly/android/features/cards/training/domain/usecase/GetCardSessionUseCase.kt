package com.nmichail.wordly.android.features.cards.training.domain.usecase

import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.training.domain.repository.CardPracticeRepository
import javax.inject.Inject

class GetCardSessionUseCase @Inject constructor(
	cardPracticeRepository: CardPracticeRepository,
) : suspend (String) -> List<CardPracticeWord> by cardPracticeRepository::getCardSession
