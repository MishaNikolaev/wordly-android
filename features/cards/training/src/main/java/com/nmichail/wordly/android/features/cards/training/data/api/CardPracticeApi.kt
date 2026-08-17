package com.nmichail.wordly.android.features.cards.training.data.api

import com.nmichail.wordly.android.features.cards.training.data.dto.CardSessionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CardPracticeApi {

	@GET("/api/gateway/cards/{id}/session")
	suspend fun getCardSession(@Path("id") id: String): CardSessionResponse
}
