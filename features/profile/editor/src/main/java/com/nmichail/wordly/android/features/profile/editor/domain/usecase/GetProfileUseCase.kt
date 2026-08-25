package com.nmichail.wordly.android.features.profile.editor.domain.usecase

import com.nmichail.wordly.android.features.profile.editor.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.editor.domain.repository.ProfileEditRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
	private val profileEditRepository: ProfileEditRepository,
) {

	suspend operator fun invoke(): UserProfile = profileEditRepository.getProfile()
}
