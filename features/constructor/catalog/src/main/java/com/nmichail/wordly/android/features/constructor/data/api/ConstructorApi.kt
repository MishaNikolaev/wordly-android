package com.nmichail.wordly.android.features.constructor.data.api

import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorCatalogResponse
import retrofit2.http.GET

interface ConstructorApi {

    @GET("/api/gateway/constructor")
    suspend fun getCatalog(): ConstructorCatalogResponse
}