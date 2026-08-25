package com.nmichail.wordly.android.features.profile.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.profile.data.api.ProfileApi
import com.nmichail.wordly.android.features.profile.data.dto.ChangePasswordRequestDto
import com.nmichail.wordly.android.features.profile.data.dto.EnglishLevelRequestDto
import com.nmichail.wordly.android.features.profile.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.data.dto.SessionDto
import com.nmichail.wordly.android.features.profile.data.dto.UpdateProfileRequestDto
import javax.inject.Inject

interface ProfileDataSource {

	suspend fun getSession(): SessionDto

	suspend fun getProfile(): ProfileDto

	suspend fun updateProfile(request: UpdateProfileRequestDto): ProfileDto

	suspend fun changePassword(request: ChangePasswordRequestDto)

	suspend fun updateEnglishLevel(request: EnglishLevelRequestDto)

	suspend fun logout()
}

class ProfileDataSourceImpl @Inject constructor(
	private val api: ProfileApi,
	private val cache: JsonCacheStore,
) : ProfileDataSource {

	override suspend fun getSession(): SessionDto =
		api.getSession()

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

	override suspend fun changePassword(request: ChangePasswordRequestDto) {
		api.changePassword(request = request)
	}

	override suspend fun updateEnglishLevel(request: EnglishLevelRequestDto) {
		api.updateEnglishLevel(request = request)
		cache.clear(key = KEY)
	}

	override suspend fun logout() {
		api.logout()
		cache.clear(key = KEY)
	}

	private companion object {
		const val KEY = "page_profile"
	}
}
