package com.nmichail.wordly.android.features.profile.presentation.reminder

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultReminderTimesComponentFactory @Inject constructor(
	private val reminderTimesStoreFactory: ReminderTimesStoreFactory,
) : ReminderTimesComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		reminderTimesRouter: ReminderTimesRouter,
	): ReminderTimesComponent =
		DefaultReminderTimesComponent(
			componentContext = componentContext,
			reminderTimesStoreFactory = reminderTimesStoreFactory,
			reminderTimesRouter = reminderTimesRouter,
		)
}