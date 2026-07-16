package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

interface NetworkSelectionComponent {

	val model: Value<State>

	fun handleSelectStand(stand: NetworkStand)

	fun handleNavigateBack()

	data class State(
		val stands: List<NetworkStand> = emptyList(),
		val selectedStand: NetworkStand = NetworkStand.DEV,
	)

	sealed interface Label {

		data object NavigateBack : Label

		data object RestartApp : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onFinished: () -> Unit,
		): NetworkSelectionComponent
	}
}
