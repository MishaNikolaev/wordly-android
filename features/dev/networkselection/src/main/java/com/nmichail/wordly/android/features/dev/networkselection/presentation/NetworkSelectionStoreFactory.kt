package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
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
			Store<NetworkSelectionStore.Intent, NetworkSelectionStore.State, NetworkSelectionStore.Label> by storeFactory.create(
				name = "NetworkSelectionStore",
				initialState = NetworkSelectionStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Initialize),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Initialize : Action
	}

	private sealed interface Msg {

		data class Initialized(
			val stands: List<NetworkStand>,
			val selectedStand: NetworkStand,
		) : Msg

		data class StandSelected(val stand: NetworkStand) : Msg
	}

	private object ReducerImpl : Reducer<NetworkSelectionStore.State, Msg> {

		override fun NetworkSelectionStore.State.reduce(msg: Msg): NetworkSelectionStore.State =
			when (msg) {
				is Msg.Initialized -> NetworkSelectionStore.State.Content(
					stands = msg.stands,
					selectedStand = msg.selectedStand,
				)
				is Msg.StandSelected -> (this as? NetworkSelectionStore.State.Content)
					?.copy(selectedStand = msg.stand)
					?: this
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			NetworkSelectionStore.Intent,
			Action,
			NetworkSelectionStore.State,
			Msg,
			NetworkSelectionStore.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Initialize -> dispatch(
					Msg.Initialized(
						stands = getNetworkStandsUseCase(),
						selectedStand = getSelectedNetworkStandUseCase(),
					),
				)
			}
		}

		override fun executeIntent(intent: NetworkSelectionStore.Intent) {
			when (intent) {
				is NetworkSelectionStore.Intent.SelectStand -> handleSelectStand(intent.stand)
				NetworkSelectionStore.Intent.NavigateBack -> publish(NetworkSelectionStore.Label.NavigateBack)
			}
		}

		private fun handleSelectStand(stand: NetworkStand) {
			val content = state() as? NetworkSelectionStore.State.Content ?: return
			if (stand == content.selectedStand) return

			setNetworkStandUseCase(stand)
			clearAuthTokensUseCase()
			dispatch(Msg.StandSelected(stand))
			publish(NetworkSelectionStore.Label.RestartApp)
		}
	}
}
