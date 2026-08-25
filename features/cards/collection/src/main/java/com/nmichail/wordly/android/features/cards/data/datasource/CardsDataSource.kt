package com.nmichail.wordly.android.features.cards.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.cards.data.api.CardsApi
import com.nmichail.wordly.android.features.cards.data.dto.CardsResponse
import javax.inject.Inject

interface CardsDataSource {

	suspend fun getCards(): CardsResponse
}

class CardsDataSourceImpl @Inject constructor(
	private val api: CardsApi,
	private val cache: JsonCacheStore,
) : CardsDataSource {

	override suspend fun getCards(): CardsResponse =
		cache.getOrFetch(
			key = KEY,
			type = CardsResponse::class.java,
		) {
			api.getCards()
		}

	private companion object {
		const val KEY = "page_cards"
	}
}
