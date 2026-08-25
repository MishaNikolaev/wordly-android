package com.nmichail.wordly.android.features.profile.reminders.data.mapper

import com.nmichail.wordly.android.features.profile.reminders.data.dto.ProfileDto
import com.nmichail.wordly.android.features.profile.reminders.data.dto.UpdateProfileRequestDto
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.NotificationTimeSlot
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UpdateProfileParams
import com.nmichail.wordly.android.features.profile.reminders.domain.entity.UserProfile

private const val DEFAULT_DAILY_GOAL_WORDS = 10

fun ProfileDto.toDomain(): UserProfile =
	UserProfile(
		id = id,
		email = email,
		firstName = firstName,
		lastName = lastName,
		englishLevel = englishLevel,
		dailyGoal = DailyGoal(wordsPerDay = dailyGoalWords ?: DEFAULT_DAILY_GOAL_WORDS),
		notificationTimes = (notificationTimes ?: emptyList()).map { time ->
			NotificationTimeSlot(time = time)
		},
	)

fun UpdateProfileParams.toDto(): UpdateProfileRequestDto =
	UpdateProfileRequestDto(
		firstName = firstName,
		lastName = lastName,
		englishLevel = englishLevel,
		dailyGoalWords = dailyGoalWords,
		notificationTimes = notificationTimes,
	)
