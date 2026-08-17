package com.nmichail.wordly.android.features.cards.data.repository

import com.nmichail.wordly.android.features.cards.data.api.CardsApi
import com.nmichail.wordly.android.features.cards.data.mapper.toEntity
import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.repository.CardsRepository
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(
	private val cardsApi: CardsApi,
) : CardsRepository {

	override suspend fun getCards(): Cards =
		cardsApi.getCards().toEntity()
}
