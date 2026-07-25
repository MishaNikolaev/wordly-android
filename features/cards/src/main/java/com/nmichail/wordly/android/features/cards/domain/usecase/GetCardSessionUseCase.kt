package com.nmichail.wordly.android.features.cards.domain.usecase

import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.domain.repository.CardsRepository
import javax.inject.Inject

class GetCardSessionUseCase @Inject constructor(
	cardsRepository: CardsRepository,
) : suspend (String) -> List<CardPracticeWord> by cardsRepository::getCardSession
