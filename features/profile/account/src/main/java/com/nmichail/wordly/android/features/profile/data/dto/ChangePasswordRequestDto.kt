package com.nmichail.wordly.android.features.profile.data.dto

data class ChangePasswordRequestDto(
	val currentPassword: String,
	val newPassword: String,
)
