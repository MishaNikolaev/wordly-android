package com.nmichail.wordly.android.features.profile.reminders.data.api

import com.nmichail.wordly.android.features.profile.reminders.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.reminders.data.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ReminderTimesApi {

	@GET("/api/gateway/profile")
	suspend fun getProfile(): ProfileDto

	@PATCH("/api/gateway/profile")
	suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ProfileDto
}
