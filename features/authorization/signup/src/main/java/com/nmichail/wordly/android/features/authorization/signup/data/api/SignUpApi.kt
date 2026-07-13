package com.nmichail.wordly.android.features.authorization.signup.data.api

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.features.authorization.signup.data.dto.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

	@POST("/api/gateway/registration")
	suspend fun register(@Body body: SignUpRequest): AuthTokensResponse
}
