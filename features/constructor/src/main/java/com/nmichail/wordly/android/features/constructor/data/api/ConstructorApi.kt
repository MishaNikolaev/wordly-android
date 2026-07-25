package com.nmichail.wordly.android.features.constructor.data.api

import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorCatalogResponse
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorSessionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ConstructorApi {

	@GET("/api/gateway/constructor")
	suspend fun getCatalog(): ConstructorCatalogResponse

	@GET("/api/gateway/constructor/{id}/session")
	suspend fun getSession(@Path("id") id: String): ConstructorSessionResponse
}