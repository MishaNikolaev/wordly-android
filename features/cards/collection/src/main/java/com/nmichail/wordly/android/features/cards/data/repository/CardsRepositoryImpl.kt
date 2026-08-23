package com.nmichail.wordly.android.features.cards.data.repository

import com.nmichail.wordly.android.features.cards.data.datasource.CardsDataSource
import com.nmichail.wordly.android.features.cards.data.mapper.toEntity
import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.repository.CardsRepository
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(
	private val dataSource: CardsDataSource,
) : CardsRepository {

	override suspend fun getCards(): Cards =
		dataSource.getCards().toEntity()
}
