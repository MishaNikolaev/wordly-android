package com.nmichail.wordly.android.features.profile.editor.domain.usecase

import com.nmichail.wordly.android.features.profile.editor.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.editor.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.editor.domain.repository.ProfileEditRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
	private val profileEditRepository: ProfileEditRepository,
) {

	suspend operator fun invoke(params: UpdateProfileParams): UserProfile =
		profileEditRepository.updateProfile(params = params)
}
