package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

interface NetworkSelectionStore :
	Store<NetworkSelectionStore.Intent, NetworkSelectionStore.State, NetworkSelectionStore.Label> {

	data class State(
		val stands: List<NetworkStand> = emptyList(),
		val selectedStand: NetworkStand = NetworkStand.DEV,
	)

	sealed interface Intent {

		data class SelectStand(val stand: NetworkStand) : Intent

		data object NavigateBack : Intent
	}

	sealed interface Label {

		data object NavigateBack : Label

		data object RestartApp : Label
	}
}
