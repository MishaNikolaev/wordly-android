package com.nmichail.wordly.android.features.cards.domain.repository

import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.domain.entity.Cards

interface CardsRepository {

	suspend fun getCards(): Cards

	suspend fun getCardSession(id: String): List<CardPracticeWord>
}
