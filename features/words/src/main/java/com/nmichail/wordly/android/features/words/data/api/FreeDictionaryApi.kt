package com.nmichail.wordly.android.features.words.data.api

import com.nmichail.wordly.android.features.words.data.dto.DictionaryEntryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FreeDictionaryApi {

	@GET("/api/v2/entries/en/{word}")
	suspend fun lookup(
		@Path("word") word: String,
	): List<DictionaryEntryDto>
}