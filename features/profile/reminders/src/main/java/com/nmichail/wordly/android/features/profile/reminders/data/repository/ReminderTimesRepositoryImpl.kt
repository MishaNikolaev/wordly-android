package com.nmichail.wordly.android.features.profile.reminders.data.repository

import com.nmichail.wordly.android.features.profile.reminders.data.datasource.ReminderTimesDataSource
import com.nmichail.wordly.android.features.profile.reminders.data.mapper.toDomain
import com.nmichail.wordly.android.features.profile.reminders.data.mapper.toDto
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.reminders.domain.repository.ReminderTimesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderTimesRepositoryImpl @Inject constructor(
	private val dataSource: ReminderTimesDataSource,
) : ReminderTimesRepository {

	override suspend fun getProfile(): UserProfile =
		dataSource.getProfile().toDomain()

	override suspend fun updateProfile(params: UpdateProfileParams): UserProfile =
		dataSource.updateProfile(request = params.toDto()).toDomain()
}
