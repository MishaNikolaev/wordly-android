package com.nmichail.wordly.android.features.home.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.usecase.GetHomeUseCase
import javax.inject.Inject

internal class HomeStoreFactory @Inject constructor(
	private val getHomeUseCase: GetHomeUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): HomeStore =
		object :
			HomeStore,
			Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> by storeFactory.create(
				name = "HomeStore",
				initialState = HomeStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Init : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class HomeLoaded(val home: Home) : Msg

		data object SetError : Msg
	}

	private object ReducerImpl : Reducer<HomeStore.State, Msg> {

		override fun HomeStore.State.reduce(msg: Msg): HomeStore.State =
			when (msg) {
				Msg.Loading -> HomeStore.State.Loading
				is Msg.HomeLoaded -> HomeStore.State.Content(
					firstName = msg.home.firstName,
					streakDays = msg.home.streakDays,
					wordsToReview = msg.home.wordsToReview,
					estimatedMinutes = msg.home.estimatedMinutes,
					reviewStreakDays = msg.home.reviewStreakDays,
					trainings = msg.home.trainings,
				)
				Msg.SetError -> HomeStore.State.Error
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			HomeStore.Intent,
			Action,
			HomeStore.State,
			Msg,
			HomeStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Init -> loadHome()
			}
		}

		override fun executeIntent(intent: HomeStore.Intent) {
			when (intent) {
				HomeStore.Intent.Retry -> loadHome()
				HomeStore.Intent.StartReview -> publish(HomeStore.Label.StartReview)
				HomeStore.Intent.OpenCards -> publish(HomeStore.Label.OpenCards)
				HomeStore.Intent.OpenConstructor -> publish(HomeStore.Label.OpenConstructor)
				HomeStore.Intent.OpenBooks -> publish(HomeStore.Label.OpenBooks)
				HomeStore.Intent.OpenMovies -> publish(HomeStore.Label.OpenMovies)
				HomeStore.Intent.OpenRecap -> publish(HomeStore.Label.OpenRecap)
			}
		}

		private fun loadHome() {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					dispatch(Msg.HomeLoaded(home = getHomeUseCase()))
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}
	}
}
