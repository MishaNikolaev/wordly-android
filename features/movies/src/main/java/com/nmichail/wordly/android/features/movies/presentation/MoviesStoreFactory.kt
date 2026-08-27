package com.nmichail.wordly.android.features.movies.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import javax.inject.Inject

internal class MoviesStoreFactory @Inject constructor() {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): MoviesStore =
		object :
			MoviesStore,
			Store<MoviesStore.Intent, MoviesStore.State, MoviesStore.Label> by storeFactory.create(
				name = "MoviesStore",
				initialState = MoviesStore.State.Content,
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action

	private sealed interface Msg

	private object ReducerImpl : Reducer<MoviesStore.State, Msg> {

		override fun MoviesStore.State.reduce(msg: Msg): MoviesStore.State = this
	}

	private class ExecutorImpl :
		BaseCoroutineExecutor<
			MoviesStore.Intent,
			Action,
			MoviesStore.State,
			Msg,
			MoviesStore.Label,
			>() {

		override fun executeIntent(intent: MoviesStore.Intent) {
			when (intent) {
				MoviesStore.Intent.Back -> publish(MoviesStore.Label.Close)
			}
		}
	}
}