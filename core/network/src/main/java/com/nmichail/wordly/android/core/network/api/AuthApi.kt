package com.nmichail.wordly.android.core.network.api

import com.nmichail.wordly.android.core.network.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.network.dto.SignInRequest
import com.nmichail.wordly.android.core.network.dto.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

	@POST(OpGateway.OP_GATEWAY_AUTHORIZATION)
	suspend fun authorize(@Body body: SignInRequest): AuthTokensResponse

	@POST(OpGateway.OP_GATEWAY_REGISTRATION)
	suspend fun register(@Body body: SignUpRequest): AuthTokensResponse
}
