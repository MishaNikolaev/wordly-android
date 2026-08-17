package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

interface NetworkSelectionStore :
	Store<NetworkSelectionStore.Intent, NetworkSelectionStore.State, NetworkSelectionStore.Label> {

	sealed interface State {

		data object Initial : State

		data class Content(
			val stands: List<NetworkStand>,
			val selectedStand: NetworkStand,
		) : State
	}

	sealed interface Label {

		data object NavigateBack : Label

		data object RestartApp : Label
	}

	sealed interface Intent {

		data class SelectStand(val stand: NetworkStand) : Intent

		data object NavigateBack : Intent
	}
}
