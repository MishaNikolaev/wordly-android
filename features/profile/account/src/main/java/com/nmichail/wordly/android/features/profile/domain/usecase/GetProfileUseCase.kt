package com.nmichail.wordly.android.features.profile.domain.usecase

import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
	private val profileRepository: ProfileRepository,
) {

	suspend operator fun invoke(): UserProfile = profileRepository.getProfile()
}