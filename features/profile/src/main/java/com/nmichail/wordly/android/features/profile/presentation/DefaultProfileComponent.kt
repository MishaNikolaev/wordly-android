package com.nmichail.wordly.android.features.profile.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal

internal class DefaultProfileComponent(
	componentContext: ComponentContext,
	profileStoreFactory: ProfileStoreFactory,
	val onOpenEdit: () -> Unit,
) : ComponentContext by componentContext,
	ProfileComponent {

	private val store: ProfileStore = instanceKeeper.getStore {
		profileStoreFactory.create()
	}

	override val model = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					ProfileStore.Label.OpenEdit -> onOpenEdit()
				}
			}
		}
	}

	override fun handleRetry() {
		store.accept(ProfileStore.Intent.Retry)
	}

	override fun handleOpenEdit() {
		store.accept(ProfileStore.Intent.OpenEdit)
	}

	override fun handleUpdateLevel(level: String) {
		store.accept(ProfileStore.Intent.UpdateLevel(level = level))
	}

	override fun handleToggleNotificationsEnabled() {
		store.accept(ProfileStore.Intent.ToggleNotificationsEnabled)
	}

	override fun handleUpdateDailyGoal(goal: DailyGoal) {
		store.accept(ProfileStore.Intent.UpdateDailyGoal(goal = goal))
	}

	override fun handleUpdateNotificationTimes(times: List<String>) {
		store.accept(ProfileStore.Intent.UpdateNotificationTimes(times = times))
	}

	override fun handleSetThemeMode(mode: AppThemeMode) {
		store.accept(ProfileStore.Intent.SetThemeMode(mode = mode))
	}

	override fun handleLogout() {
		store.accept(ProfileStore.Intent.Logout)
	}

	override fun handleRefresh() {
		store.accept(ProfileStore.Intent.Refresh)
	}
}
