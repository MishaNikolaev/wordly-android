package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile

interface ProfileStore :
	Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Loading : State

		data class Content(
			val profile: UserProfile,
			val notificationsEnabled: Boolean,
			val loggingOut: Boolean,
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object OpenEdit : Label

		data object OpenReminderTimes : Label

		data object OpenNetworkSelection : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data object Refresh : Intent

		data object OpenEdit : Intent

		data object OpenReminderTimes : Intent

		data object OpenNetworkSelection : Intent

		data class UpdateLevel(val level: String) : Intent

		data object ToggleNotificationsEnabled : Intent

		data class UpdateDailyGoal(val goal: DailyGoal) : Intent

		data class SetThemeMode(val mode: AppThemeMode) : Intent

		data object Logout : Intent
	}
}