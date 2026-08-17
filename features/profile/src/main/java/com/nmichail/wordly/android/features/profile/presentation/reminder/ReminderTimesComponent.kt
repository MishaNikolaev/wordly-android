package com.nmichail.wordly.android.features.profile.presentation.reminder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface ReminderTimesComponent {

	val model: Value<ReminderTimesStore.State>

	fun handleBack()

	fun handleRetry()

	fun handleToggleTime(time: String)

	fun handleSave()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			reminderTimesRouter: ReminderTimesRouter,
		): ReminderTimesComponent
	}
}