package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal

interface ProfileComponent {

	val model: Value<ProfileStore.State>

	fun handleRetry()

	fun handleOpenEdit()

	fun handleUpdateLevel(level: String)

	fun handleToggleNotificationsEnabled()

	fun handleUpdateDailyGoal(goal: DailyGoal)

	fun handleUpdateNotificationTimes(times: List<String>)

	fun handleSetThemeMode(mode: AppThemeMode)

	fun handleLogout()

	fun handleRefresh()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onOpenEdit: () -> Unit,
		): ProfileComponent
	}
}
