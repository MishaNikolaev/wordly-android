package com.nmichail.wordly.android.features.words.data.api

import com.nmichail.wordly.android.features.words.data.dto.MyMemoryResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MyMemoryApi {

	@GET("get")
	suspend fun translate(
		@Query("q") query: String,
		@Query("langpair") langPair: String = "en|ru",
	): MyMemoryResponseDto
}
