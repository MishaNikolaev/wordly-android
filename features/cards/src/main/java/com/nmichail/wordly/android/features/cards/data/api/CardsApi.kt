package com.nmichail.wordly.android.features.cards.data.api

import com.nmichail.wordly.android.features.cards.data.dto.CardSessionResponse
import com.nmichail.wordly.android.features.cards.data.dto.CardsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CardsApi {

	@GET("/api/gateway/cards")
	suspend fun getCards(): CardsResponse

	@GET("/api/gateway/cards/{id}/session")
	suspend fun getCardSession(@Path("id") id: String): CardSessionResponse
}