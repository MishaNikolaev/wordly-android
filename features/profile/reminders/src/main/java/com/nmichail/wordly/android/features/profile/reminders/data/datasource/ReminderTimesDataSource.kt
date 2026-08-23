package com.nmichail.wordly.android.features.profile.reminders.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.profile.reminders.data.api.ReminderTimesApi
import com.nmichail.wordly.android.features.profile.reminders.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.reminders.data.dto.UpdateProfileRequestDto
import javax.inject.Inject

interface ReminderTimesDataSource {

	suspend fun getProfile(): ProfileDto

	suspend fun updateProfile(request: UpdateProfileRequestDto): ProfileDto
}

class ReminderTimesDataSourceImpl @Inject constructor(
	private val api: ReminderTimesApi,
	private val cache: JsonCacheStore,
) : ReminderTimesDataSource {

	override suspend fun getProfile(): ProfileDto =
		cache.getOrFetch(
			key = KEY,
			type = ProfileDto::class.java,
		) {
			api.getProfile()
		}

	override suspend fun updateProfile(request: UpdateProfileRequestDto): ProfileDto {
		val dto = api.updateProfile(request = request)
		cache.put(key = KEY, value = dto)
		return dto
	}

	private companion object {
		const val KEY = "page_profile"
	}
}
