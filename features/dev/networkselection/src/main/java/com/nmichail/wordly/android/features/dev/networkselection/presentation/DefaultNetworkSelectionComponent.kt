package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

internal class DefaultNetworkSelectionComponent(
	componentContext: ComponentContext,
	private val networkSelectionStoreFactory: NetworkSelectionStoreFactory,
	private val onFinished: () -> Unit,
	private val networkSelectionRouter: NetworkSelectionRouter,
) : ComponentContext by componentContext,
	NetworkSelectionComponent {

	private val store: NetworkSelectionStore = instanceKeeper.getStore {
		networkSelectionStoreFactory.create()
	}

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					NetworkSelectionComponent.Label.NavigateBack -> onFinished()
					NetworkSelectionComponent.Label.RestartApp -> networkSelectionRouter.restartApp()
				}
			}
		} catch {
			// ignored
		}
	}

	override val model: Value<NetworkSelectionComponent.State> = store.asValue()

	override fun handleSelectStand(stand: NetworkStand) {
		store.accept(NetworkSelectionStore.Intent.SelectStand(stand))
	}

	override fun handleNavigateBack() {
		store.accept(NetworkSelectionStore.Intent.NavigateBack)
	}
}