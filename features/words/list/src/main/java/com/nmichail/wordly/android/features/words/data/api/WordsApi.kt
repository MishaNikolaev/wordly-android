package com.nmichail.wordly.android.features.words.data.api

import com.nmichail.wordly.android.features.words.data.dto.AddToReviewBody
import com.nmichail.wordly.android.features.words.data.dto.AddWordBody
import com.nmichail.wordly.android.features.words.data.dto.UpdateWordStatusBody
import com.nmichail.wordly.android.features.words.data.dto.VocabularyLookupDto
import com.nmichail.wordly.android.features.words.data.dto.WordsCatalogDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WordsApi {

	@GET("/api/words")
	suspend fun getWords(
		@Query("status") status: String? = null,
		@Query("query") query: String? = null,
	): WordsCatalogDto

	@GET("/api/gateway/vocabulary/lookup")
	suspend fun lookupVocabulary(
		@Query("q") query: String,
	): VocabularyLookupDto

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