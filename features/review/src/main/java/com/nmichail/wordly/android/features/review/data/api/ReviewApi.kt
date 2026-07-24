package com.nmichail.wordly.android.features.review.data.api

import com.nmichail.wordly.android.features.review.data.dto.ReviewAnswerRequest
import com.nmichail.wordly.android.features.review.data.dto.ReviewSessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReviewApi {

	@GET("/api/review/session")
	suspend fun getSession(): ReviewSessionResponse

	@POST("/api/review/answer")
	suspend fun submitAnswer(
		@Body request: ReviewAnswerRequest,
	)
}