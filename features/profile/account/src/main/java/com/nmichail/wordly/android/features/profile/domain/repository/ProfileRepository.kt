package com.nmichail.wordly.android.features.profile.domain.repository

import com.nmichail.wordly.android.features.profile.domain.entity.Session
import com.nmichail.wordly.android.features.profile.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile

interface ProfileRepository {

	suspend fun getSession(): Session

	suspend fun getProfile(): UserProfile

	suspend fun updateProfile(params: UpdateProfileParams): UserProfile

	suspend fun changePassword(
		currentPassword: String,
		newPassword: String,
	)

	suspend fun updateEnglishLevel(level: String)

	suspend fun logout()
}
