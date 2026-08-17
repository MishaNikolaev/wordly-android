package com.nmichail.wordly.android.features.constructor.practice.data.api

import com.nmichail.wordly.android.features.constructor.practice.data.dto.ConstructorSessionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ConstructorPracticeApi {

    @GET("/api/gateway/constructor/{id}/session")
    suspend fun getSession(@Path("id") id: String): ConstructorSessionResponse
}