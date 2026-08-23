package com.nmichail.wordly.android.features.profile.editor.domain.repository

import com.nmichail.wordly.android.features.profile.editor.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.editor.domain.entity.UserProfile

interface ProfileEditRepository {

	suspend fun getProfile(): UserProfile

	suspend fun updateProfile(params: UpdateProfileParams): UserProfile
}
