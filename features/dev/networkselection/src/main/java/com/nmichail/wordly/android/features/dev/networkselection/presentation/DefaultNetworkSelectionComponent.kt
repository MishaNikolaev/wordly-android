package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand

class DefaultNetworkSelectionComponent(
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
			store.labels.collect { label ->
				when (label) {
					NetworkSelectionStore.Label.NavigateBack -> onFinished()
					NetworkSelectionStore.Label.RestartApp -> networkSelectionRouter.restartApp()
				}
			}
		} catch {
			// ignored — same as cft_shift: navigation side-effects should not crash the store
		}
	}

	override val model: Value<NetworkSelectionStore.State> = store.asValue()

	override fun handleSelectStand(stand: NetworkStand) {
		store.accept(NetworkSelectionStore.Intent.SelectStand(stand))
	}

	override fun handleNavigateBack() {
		store.accept(NetworkSelectionStore.Intent.NavigateBack)
	}
}
