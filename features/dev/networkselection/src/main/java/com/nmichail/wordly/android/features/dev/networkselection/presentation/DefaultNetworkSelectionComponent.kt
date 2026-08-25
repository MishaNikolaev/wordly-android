package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class DefaultNetworkSelectionComponent @AssistedInject constructor(
	private val networkSelectionStoreFactory: NetworkSelectionStoreFactory,
	private val networkSelectionRouter: NetworkSelectionRouter,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("onFinished") private val onFinished: () -> Unit,
) : ComponentContext by componentContext,
	NetworkSelectionComponent {

	private val store: NetworkSelectionStore = instanceKeeper.getStore {
		networkSelectionStoreFactory.create()
	}

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					NetworkSelectionStore.Label.NavigateBack -> onFinished()
					NetworkSelectionStore.Label.RestartApp -> networkSelectionRouter.restartApp()
				}
			}
		}
	}

	override val model: Value<NetworkSelectionStore.State> = store.asValue()

	override fun handleSelectStand(stand: NetworkStand) {
		store.accept(NetworkSelectionStore.Intent.SelectStand(stand))
	}

	override fun handleNavigateBack() {
		store.accept(NetworkSelectionStore.Intent.NavigateBack)
	}

	@AssistedFactory
	fun interface Factory : NetworkSelectionComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("onFinished") onFinished: () -> Unit,
		): DefaultNetworkSelectionComponent
	}
}
