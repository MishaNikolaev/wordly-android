package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlot

internal interface ProfileStore :
	Store<ProfileStore.Intent, ProfileComponent.State, ProfileComponent.Label> {

	sealed interface Intent {

		data object Retry : Intent

		data object Refresh : Intent

		data object OpenEdit : Intent

		data object OpenLevel : Intent

		data class ConfirmLevel(val level: String) : Intent

		data object DismissLevel : Intent

		data object ToggleNotificationsEnabled : Intent

		data object OpenDailyGoal : Intent

		data class ConfirmDailyGoal(val goal: DailyGoal) : Intent

		data object DismissDailyGoal : Intent

		data object OpenNotifications : Intent

		data class ToggleNotification(val slot: NotificationTimeSlot) : Intent

		data object ConfirmNotifications : Intent

		data object DismissNotifications : Intent

		data object OpenTheme : Intent

		data class SelectTheme(val mode: AppThemeMode) : Intent

		data object ConfirmTheme : Intent

		data object DismissTheme : Intent

		data object OpenLogout : Intent

		data object ConfirmLogout : Intent

		data object DismissLogout : Intent
	}
}
