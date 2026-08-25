package com.nmichail.wordly.android.features.profile.domain.usecase

import com.nmichail.wordly.android.features.profile.domain.entity.Session
import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Validates current access token against backend, same role as cft_shift GetSessionUseCase.
 */
class GetSessionUseCase @Inject constructor(
	private val profileRepository: ProfileRepository,
) {

	suspend operator fun invoke(): Session =
		profileRepository.getSession()
}
