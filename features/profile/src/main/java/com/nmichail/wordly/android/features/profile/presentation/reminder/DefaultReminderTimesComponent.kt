package com.nmichail.wordly.android.features.profile.presentation.reminder

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue

internal class DefaultReminderTimesComponent(
	componentContext: ComponentContext,
	reminderTimesStoreFactory: ReminderTimesStoreFactory,
	private val reminderTimesRouter: ReminderTimesRouter,
) : ComponentContext by componentContext,
	ReminderTimesComponent {

	private val store: ReminderTimesStore = instanceKeeper.getStore {
		reminderTimesStoreFactory.create()
	}

	override val model = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					ReminderTimesStore.Label.Close -> reminderTimesRouter.navigateBack()
				}
			}
		}
	}

	override fun handleBack() {
		store.accept(ReminderTimesStore.Intent.Back)
	}

	override fun handleRetry() {
		store.accept(ReminderTimesStore.Intent.Retry)
	}

	override fun handleToggleTime(time: String) {
		store.accept(ReminderTimesStore.Intent.ToggleTime(time = time))
	}

	override fun handleSave() {
		store.accept(ReminderTimesStore.Intent.Save)
	}
}