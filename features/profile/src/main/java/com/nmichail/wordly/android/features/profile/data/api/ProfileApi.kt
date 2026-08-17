package com.nmichail.wordly.android.features.profile.data.api

import com.nmichail.wordly.android.features.profile.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.data.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileApi {

	@GET("/api/gateway/profile")
	suspend fun getProfile(): ProfileDto

	@PATCH("/api/gateway/profile")
	suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ProfileDto

	@POST("/api/gateway/logout")
	suspend fun logout()
}
