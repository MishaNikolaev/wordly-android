package com.nmichail.wordly.android.features.profile.domain.usecase

import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
	private val profileRepository: ProfileRepository,
) {

	suspend operator fun invoke(
		currentPassword: String,
		newPassword: String,
	) {
		profileRepository.changePassword(
			currentPassword = currentPassword,
			newPassword = newPassword,
		)
	}
}
