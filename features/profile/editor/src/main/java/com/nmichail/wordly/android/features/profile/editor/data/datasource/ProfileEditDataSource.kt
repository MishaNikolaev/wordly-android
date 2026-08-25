package com.nmichail.wordly.android.features.profile.editor.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.profile.editor.data.api.ProfileEditApi
import com.nmichail.wordly.android.features.profile.editor.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.editor.data.dto.UpdateProfileRequestDto
import javax.inject.Inject

interface ProfileEditDataSource {

	suspend fun getProfile(): ProfileDto

	suspend fun updateProfile(request: UpdateProfileRequestDto): ProfileDto
}

class ProfileEditDataSourceImpl @Inject constructor(
	private val api: ProfileEditApi,
	private val cache: JsonCacheStore,
) : ProfileEditDataSource {

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
