package com.nmichail.wordly.android.features.profile.data.api

import com.nmichail.wordly.android.features.profile.data.dto.ChangePasswordRequestDto
import com.nmichail.wordly.android.features.profile.data.dto.EnglishLevelRequestDto
import com.nmichail.wordly.android.features.profile.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.data.dto.SessionDto
import com.nmichail.wordly.android.features.profile.data.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileApi {

	/** Like cft_shift GET /api/gateway/authentication/session — validates token & returns identity. */
	@GET("/api/gateway/session")
	suspend fun getSession(): SessionDto

	@GET("/api/gateway/profile")
	suspend fun getProfile(): ProfileDto

	@PATCH("/api/gateway/profile")
	suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ProfileDto

	@PATCH("/api/gateway/profile/password")
	suspend fun changePassword(@Body request: ChangePasswordRequestDto)

	@POST("/api/gateway/level")
	suspend fun updateEnglishLevel(@Body request: EnglishLevelRequestDto)

	@POST("/api/gateway/logout")
	suspend fun logout()
}
