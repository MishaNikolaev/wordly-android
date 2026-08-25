package com.nmichail.wordly.android.features.authorization.signin.data.api

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.features.authorization.signin.data.dto.PasswordResetRequest
import com.nmichail.wordly.android.features.authorization.signin.data.dto.RefreshRequest
import com.nmichail.wordly.android.features.authorization.signin.data.dto.SignInRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInApi {

	@POST("/api/gateway/authorization")
	suspend fun authorize(@Body body: SignInRequest): AuthTokensResponse

	@POST("/api/gateway/refresh")
	suspend fun refresh(@Body body: RefreshRequest): AuthTokensResponse

	@POST("/api/gateway/password/reset")
	suspend fun requestPasswordReset(@Body body: PasswordResetRequest)
}
