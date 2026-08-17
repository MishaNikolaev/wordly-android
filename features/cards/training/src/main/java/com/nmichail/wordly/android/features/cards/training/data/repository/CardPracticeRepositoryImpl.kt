package com.nmichail.wordly.android.features.cards.training.data.repository

import com.nmichail.wordly.android.features.cards.training.data.api.CardPracticeApi
import com.nmichail.wordly.android.features.cards.training.data.mapper.toEntity
import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.training.domain.repository.CardPracticeRepository
import javax.inject.Inject

class CardPracticeRepositoryImpl @Inject constructor(
	private val cardPracticeApi: CardPracticeApi,
) : CardPracticeRepository {

	override suspend fun getCardSession(id: String): List<CardPracticeWord> =
		cardPracticeApi.getCardSession(id).toEntity()
}
