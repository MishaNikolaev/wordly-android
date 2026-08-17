package com.nmichail.wordly.android.features.cards.training.domain.repository

import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeWord

interface CardPracticeRepository {

	suspend fun getCardSession(id: String): List<CardPracticeWord>
}
