package com.nmichail.wordly.android.features.profile.data.repository

import com.nmichail.wordly.android.features.profile.data.api.ProfileApi
import com.nmichail.wordly.android.features.profile.data.mapper.toDomain
import com.nmichail.wordly.android.features.profile.data.mapper.toDto
import com.nmichail.wordly.android.features.profile.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
	private val profileApi: ProfileApi,
) : ProfileRepository {

	override suspend fun getProfile(): UserProfile =
		profileApi.getProfile().toDomain()

	override suspend fun updateProfile(params: UpdateProfileParams): UserProfile =
		profileApi.updateProfile(request = params.toDto()).toDomain()

	override suspend fun logout() {
		try {
			profileApi.logout()
		} catch (_: Exception) {
			// logout must proceed even if the request fails
		}
	}
}
