package com.nmichail.wordly.android.core.network.api

import retrofit2.http.Body
import retrofit2.http.POST

interface EnglishLevelApi {

	@POST(OpGateway.OP_GATEWAY_ENGLISH_LEVEL)
	suspend fun updateEnglishLevel(@Body request: EnglishLevelRequest)
}
