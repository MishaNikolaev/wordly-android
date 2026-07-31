package com.nmichail.wordly.android.features.profile.domain.entity

data class NotificationTimeSlot(
	val time: String,
)

object NotificationTimeSlots {

	val options: List<NotificationTimeSlot> = listOf(
		NotificationTimeSlot(time = "09:00"),
		NotificationTimeSlot(time = "12:00"),
		NotificationTimeSlot(time = "18:00"),
		NotificationTimeSlot(time = "21:00"),
	)
}