package com.nmichail.wordly.android.features.cards.domain.usecase

import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.repository.CardsRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(
	cardsRepository: CardsRepository,
) : suspend () -> Cards by cardsRepository::getCards