package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

internal interface NetworkSelectionStore :
	Store<NetworkSelectionStore.Intent, NetworkSelectionComponent.State, NetworkSelectionComponent.Label> {

	sealed interface Intent {

		data class SelectStand(val stand: NetworkStand) : Intent

		data object NavigateBack : Intent
	}
}
