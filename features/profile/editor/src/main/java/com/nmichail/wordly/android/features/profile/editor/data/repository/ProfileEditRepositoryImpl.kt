package com.nmichail.wordly.android.features.profile.editor.data.repository

import com.nmichail.wordly.android.features.profile.editor.data.datasource.ProfileEditDataSource
import com.nmichail.wordly.android.features.profile.editor.data.mapper.toDomain
import com.nmichail.wordly.android.features.profile.editor.data.mapper.toDto
import com.nmichail.wordly.android.features.profile.editor.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.editor.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.editor.domain.repository.ProfileEditRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileEditRepositoryImpl @Inject constructor(
	private val dataSource: ProfileEditDataSource,
) : ProfileEditRepository {

	override suspend fun getProfile(): UserProfile =
		dataSource.getProfile().toDomain()

	override suspend fun updateProfile(params: UpdateProfileParams): UserProfile =
		dataSource.updateProfile(request = params.toDto()).toDomain()
}
