package com.nmichail.wordly.android.features.profile.reminders.domain.repository

import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UserProfile

interface ReminderTimesRepository {

	suspend fun getProfile(): UserProfile

	suspend fun updateProfile(params: UpdateProfileParams): UserProfile
}
