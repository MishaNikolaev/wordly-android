package com.nmichail.wordly.android.features.recap.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import javax.inject.Inject

internal class RecapStoreFactory @Inject constructor() {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): RecapStore =
		object :
			RecapStore,
			Store<RecapStore.Intent, RecapStore.State, RecapStore.Label> by storeFactory.create(
				name = "RecapStore",
				initialState = RecapStore.State.Content,
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Msg

	private object ReducerImpl : Reducer<RecapStore.State, Msg> {

		override fun RecapStore.State.reduce(msg: Msg): RecapStore.State = this
	}

	private class ExecutorImpl :
		BaseCoroutineExecutor<
			RecapStore.Intent,
			Nothing,
			RecapStore.State,
			Msg,
			RecapStore.Label,
			>() {

		override fun executeIntent(intent: RecapStore.Intent) {
			when (intent) {
				RecapStore.Intent.Back -> publish(RecapStore.Label.Close)
			}
		}
	}
}
