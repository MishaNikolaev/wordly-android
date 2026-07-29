package com.nmichail.wordly.android.features.words.data.api

import com.nmichail.wordly.android.features.words.data.dto.AddToReviewBody
import com.nmichail.wordly.android.features.words.data.dto.AddWordBody
import com.nmichail.wordly.android.features.words.data.dto.UpdateWordStatusBody
import com.nmichail.wordly.android.features.words.data.dto.WordsCatalogDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WordsApi {

	@GET("/api/words")
	suspend fun getWords(): WordsCatalogDto

	@POST("/api/words")
	suspend fun addWord(@Body body: AddWordBody)

	@POST("/api/words/{id}/status")
	suspend fun updateStatus(
		@Path("id") wordId: String,
		@Body body: UpdateWordStatusBody,
	)

	@POST("/api/words/{id}/review")
	suspend fun addToReview(
		@Path("id") wordId: String,
		@Body body: AddToReviewBody,
	)
}