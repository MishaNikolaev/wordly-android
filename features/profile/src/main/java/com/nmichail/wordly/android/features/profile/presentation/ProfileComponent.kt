package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlot
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile

@Suppress("TooManyFunctions")
interface ProfileComponent {

	val model: Value<State>

	fun handleRetry()

	fun handleOpenEdit()

	fun handleOpenLevel()

	fun handleConfirmLevel(level: String)

	fun handleDismissLevel()

	fun handleToggleNotificationsEnabled()

	fun handleOpenDailyGoal()

	fun handleConfirmDailyGoal(goal: DailyGoal)

	fun handleDismissDailyGoal()

	fun handleOpenNotifications()

	fun handleToggleNotification(slot: NotificationTimeSlot)

	fun handleConfirmNotifications()

	fun handleDismissNotifications()

	fun handleOpenTheme()

	fun handleSelectTheme(mode: AppThemeMode)

	fun handleConfirmTheme()

	fun handleDismissTheme()

	fun handleOpenLogout()

	fun handleConfirmLogout()

	fun handleDismissLogout()

	fun handleRefresh()

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val profile: UserProfile,
			val themeMode: AppThemeMode,
			val notificationsEnabled: Boolean,
			val levelDialog: LevelDialogState?,
			val dailyGoalDialog: DailyGoalDialogState?,
			val notificationsDialog: NotificationsDialogState?,
			val themeDialog: ThemeDialogState?,
			val logoutDialogVisible: Boolean,
			val loggingOut: Boolean,
		) : State
	}

	data class LevelDialogState(
		val options: List<String>,
		val selected: String,
	)

	data class DailyGoalDialogState(
		val options: List<DailyGoal>,
		val selected: DailyGoal,
	)

	data class NotificationsDialogState(
		val options: List<NotificationTimeSlot>,
		val selected: Set<String>,
	)

	data class ThemeDialogState(
		val options: List<AppThemeMode>,
		val selected: AppThemeMode,
		val initial: AppThemeMode,
	)

	sealed interface Label {

		data object OpenEdit : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onOpenEdit: () -> Unit,
		): ProfileComponent
	}
}
