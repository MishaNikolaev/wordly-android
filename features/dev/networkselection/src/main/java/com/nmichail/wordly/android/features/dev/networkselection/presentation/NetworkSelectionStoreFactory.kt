package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.GetNetworkStandsUseCase
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.GetSelectedNetworkStandUseCase
import com.nmichail.wordly.android.features.dev.networkselection.domain.usecase.SetNetworkStandUseCase
import javax.inject.Inject

internal class NetworkSelectionStoreFactory @Inject constructor(
	private val getNetworkStandsUseCase: GetNetworkStandsUseCase,
	private val getSelectedNetworkStandUseCase: GetSelectedNetworkStandUseCase,
	private val setNetworkStandUseCase: SetNetworkStandUseCase,
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): NetworkSelectionStore =
		object :
			NetworkSelectionStore,
			Store<NetworkSelectionStore.Intent, NetworkSelectionComponent.State, NetworkSelectionComponent.Label> by storeFactory.create(
				name = "NetworkSelectionStore",
				initialState = NetworkSelectionComponent.State(
					stands = getNetworkStandsUseCase(),
					selectedStand = getSelectedNetworkStandUseCase(),
				),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Msg {

		data class StandSelected(val stand: NetworkStand) : Msg
	}

	private object ReducerImpl : Reducer<NetworkSelectionComponent.State, Msg> {

		override fun NetworkSelectionComponent.State.reduce(msg: Msg): NetworkSelectionComponent.State =
			when (msg) {
				is Msg.StandSelected -> copy(selectedStand = msg.stand)
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			NetworkSelectionStore.Intent,
			Nothing,
			NetworkSelectionComponent.State,
			Msg,
			NetworkSelectionComponent.Label,
		>() {

		override fun executeIntent(intent: NetworkSelectionStore.Intent) {
			when (intent) {
				is NetworkSelectionStore.Intent.SelectStand -> handleSelectStand(intent.stand)
				NetworkSelectionStore.Intent.NavigateBack -> publish(NetworkSelectionComponent.Label.NavigateBack)
			}
		}

		private fun handleSelectStand(stand: NetworkStand) {
			if (stand == state().selectedStand) return

			setNetworkStandUseCase(stand)
			clearAuthTokensUseCase()
			dispatch(Msg.StandSelected(stand))
			publish(NetworkSelectionComponent.Label.RestartApp)
		}
	}
}
