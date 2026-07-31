package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlot

@Suppress("TooManyFunctions")
internal class DefaultProfileComponent(
	componentContext: ComponentContext,
	profileStoreFactory: ProfileStoreFactory,
	private val onOpenEdit: () -> Unit,
) : ComponentContext by componentContext,
	ProfileComponent {

	private val store: ProfileStore = instanceKeeper.getStore {
		profileStoreFactory.create()
	}

	override val model = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					ProfileComponent.Label.OpenEdit -> onOpenEdit()
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleRetry() {
		store.accept(ProfileStore.Intent.Retry)
	}

	override fun handleOpenEdit() {
		store.accept(ProfileStore.Intent.OpenEdit)
	}

	override fun handleOpenLevel() {
		store.accept(ProfileStore.Intent.OpenLevel)
	}

	override fun handleConfirmLevel(level: String) {
		store.accept(ProfileStore.Intent.ConfirmLevel(level = level))
	}

	override fun handleDismissLevel() {
		store.accept(ProfileStore.Intent.DismissLevel)
	}

	override fun handleToggleNotificationsEnabled() {
		store.accept(ProfileStore.Intent.ToggleNotificationsEnabled)
	}

	override fun handleOpenDailyGoal() {
		store.accept(ProfileStore.Intent.OpenDailyGoal)
	}

	override fun handleConfirmDailyGoal(goal: DailyGoal) {
		store.accept(ProfileStore.Intent.ConfirmDailyGoal(goal = goal))
	}

	override fun handleDismissDailyGoal() {
		store.accept(ProfileStore.Intent.DismissDailyGoal)
	}

	override fun handleOpenNotifications() {
		store.accept(ProfileStore.Intent.OpenNotifications)
	}

	override fun handleToggleNotification(slot: NotificationTimeSlot) {
		store.accept(ProfileStore.Intent.ToggleNotification(slot = slot))
	}

	override fun handleConfirmNotifications() {
		store.accept(ProfileStore.Intent.ConfirmNotifications)
	}

	override fun handleDismissNotifications() {
		store.accept(ProfileStore.Intent.DismissNotifications)
	}

	override fun handleOpenTheme() {
		store.accept(ProfileStore.Intent.OpenTheme)
	}

	override fun handleSelectTheme(mode: AppThemeMode) {
		store.accept(ProfileStore.Intent.SelectTheme(mode = mode))
	}

	override fun handleConfirmTheme() {
		store.accept(ProfileStore.Intent.ConfirmTheme)
	}

	override fun handleDismissTheme() {
		store.accept(ProfileStore.Intent.DismissTheme)
	}

	override fun handleOpenLogout() {
		store.accept(ProfileStore.Intent.OpenLogout)
	}

	override fun handleConfirmLogout() {
		store.accept(ProfileStore.Intent.ConfirmLogout)
	}

	override fun handleDismissLogout() {
		store.accept(ProfileStore.Intent.DismissLogout)
	}

	override fun handleRefresh() {
		store.accept(ProfileStore.Intent.Refresh)
	}
}
