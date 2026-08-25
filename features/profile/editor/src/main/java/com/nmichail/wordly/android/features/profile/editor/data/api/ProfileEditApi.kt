package com.nmichail.wordly.android.features.profile.editor.data.api

import com.nmichail.wordly.android.features.profile.editor.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.editor.data.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileEditApi {

	@GET("/api/gateway/profile")
	suspend fun getProfile(): ProfileDto

	@PATCH("/api/gateway/profile")
	suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ProfileDto
}
