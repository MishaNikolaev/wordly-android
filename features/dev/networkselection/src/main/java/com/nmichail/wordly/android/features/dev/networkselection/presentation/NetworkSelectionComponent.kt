package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

interface NetworkSelectionComponent {

	val model: Value<NetworkSelectionStore.State>

	fun handleSelectStand(stand: NetworkStand)

	fun handleNavigateBack()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onFinished: () -> Unit,
		): NetworkSelectionComponent
	}
}
