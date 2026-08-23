package com.nmichail.wordly.android.shared.englishlevel.data.api

import com.nmichail.wordly.android.shared.englishlevel.data.dto.EnglishLevelRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface EnglishLevelApi {

    @POST("/api/gateway/level")
    suspend fun updateEnglishLevel(@Body request: EnglishLevelRequest)
}