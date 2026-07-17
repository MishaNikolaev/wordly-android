package com.nmichail.wordly.android.features.home.data.api

import com.nmichail.wordly.android.features.home.data.dto.HomeResponse
import retrofit2.http.GET

interface HomeApi {

	@GET("/api/gateway/home")
	suspend fun getHome(): HomeResponse
}
