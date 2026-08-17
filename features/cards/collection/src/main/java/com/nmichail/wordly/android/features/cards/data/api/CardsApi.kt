package com.nmichail.wordly.android.features.cards.data.api

import com.nmichail.wordly.android.features.cards.data.dto.CardsResponse
import retrofit2.http.GET

interface CardsApi {

	@GET("/api/gateway/cards")
	suspend fun getCards(): CardsResponse
}
