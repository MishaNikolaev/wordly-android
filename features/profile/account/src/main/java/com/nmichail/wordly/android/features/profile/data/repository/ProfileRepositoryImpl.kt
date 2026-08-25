package com.nmichail.wordly.android.features.profile.data.repository

import com.nmichail.wordly.android.features.profile.data.datasource.ProfileDataSource
import com.nmichail.wordly.android.features.profile.data.dto.ChangePasswordRequestDto
import com.nmichail.wordly.android.features.profile.data.dto.EnglishLevelRequestDto
import com.nmichail.wordly.android.features.profile.data.mapper.toDomain
import com.nmichail.wordly.android.features.profile.data.mapper.toDto
import com.nmichail.wordly.android.features.profile.domain.entity.Session
import com.nmichail.wordly.android.features.profile.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
	private val dataSource: ProfileDataSource,
) : ProfileRepository {

	override suspend fun getSession(): Session {
		val dto = dataSource.getSession()
		return Session(
			userId = dto.userId,
			email = dto.email,
		)
	}

	override suspend fun getProfile(): UserProfile =
		dataSource.getProfile().toDomain()

	override suspend fun updateProfile(params: UpdateProfileParams): UserProfile =
		dataSource.updateProfile(request = params.toDto()).toDomain()

	override suspend fun changePassword(
		currentPassword: String,
		newPassword: String,
	) {
		dataSource.changePassword(
			request = ChangePasswordRequestDto(
				currentPassword = currentPassword,
				newPassword = newPassword,
			),
		)
	}

	override suspend fun updateEnglishLevel(level: String) {
		dataSource.updateEnglishLevel(request = EnglishLevelRequestDto(level = level))
	}

	override suspend fun logout() {
		dataSource.logout()
	}
}
