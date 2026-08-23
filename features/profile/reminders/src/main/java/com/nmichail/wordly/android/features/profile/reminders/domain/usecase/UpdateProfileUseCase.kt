package com.nmichail.wordly.android.features.profile.reminders.domain.usecase

import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.reminders.domain.repository.ReminderTimesRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
	private val reminderTimesRepository: ReminderTimesRepository,
) {

	suspend operator fun invoke(params: UpdateProfileParams): UserProfile =
		reminderTimesRepository.updateProfile(params = params)
}
