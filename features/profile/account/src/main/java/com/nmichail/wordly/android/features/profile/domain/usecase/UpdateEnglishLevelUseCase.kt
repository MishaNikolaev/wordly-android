package com.nmichail.wordly.android.features.profile.domain.usecase

import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateEnglishLevelUseCase @Inject constructor(
	private val profileRepository: ProfileRepository,
) {

	suspend operator fun invoke(level: String) {
		profileRepository.updateEnglishLevel(level = level)
	}
}
