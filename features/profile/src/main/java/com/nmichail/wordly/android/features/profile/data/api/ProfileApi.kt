package com.nmichail.wordly.android.features.profile.data.api

import com.nmichail.wordly.android.core.network.api.OpGateway
import com.nmichail.wordly.android.features.profile.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.data.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileApi {

	@GET(OpGateway.OP_GATEWAY_PROFILE)
	suspend fun getProfile(): ProfileDto

	@PATCH(OpGateway.OP_GATEWAY_PROFILE)
	suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ProfileDto

	@POST(OpGateway.OP_GATEWAY_LOGOUT)
	suspend fun logout()
}
