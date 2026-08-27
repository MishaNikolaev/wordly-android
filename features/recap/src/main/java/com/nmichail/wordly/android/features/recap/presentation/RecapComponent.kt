package com.nmichail.wordly.android.features.recap.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface RecapComponent {

	val model: Value<RecapStore.State>

	fun handleBack()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			recapRouter: RecapRouter,
		): RecapComponent
	}
}