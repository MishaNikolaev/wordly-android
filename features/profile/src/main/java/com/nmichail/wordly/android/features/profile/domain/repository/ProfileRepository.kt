package com.nmichail.wordly.android.features.profile.domain.repository

import com.nmichail.wordly.android.features.profile.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile

interface ProfileRepository {

	suspend fun getProfile(): UserProfile

	suspend fun updateProfile(params: UpdateProfileParams): UserProfile

	suspend fun logout()
}