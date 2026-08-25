package com.nmichail.wordly.android.features.books.detail.data.api

import com.nmichail.wordly.android.features.books.detail.data.dto.BookDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BookDetailApi {

	@GET("/api/gateway/books/{id}/detail")
	suspend fun getBookDetail(
		@Path("id") id: String,
	): BookDetailResponse
}