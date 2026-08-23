package com.nmichail.wordly.android.features.profile.reminders.domain.usecase

import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.reminders.domain.repository.ReminderTimesRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
	private val reminderTimesRepository: ReminderTimesRepository,
) {

	suspend operator fun invoke(): UserProfile = reminderTimesRepository.getProfile()
}
