package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.home.domain.entity.Month
import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.usecase.GetMonthUseCase
import com.nmichail.wordly.android.features.home.domain.usecase.GetHomeUseCase
import java.time.Clock
import java.time.YearMonth
import javax.inject.Inject

internal class HomeStoreFactory @Inject constructor(
	private val getHomeUseCase: GetHomeUseCase,
	private val getMonthUseCase: GetMonthUseCase,
	private val clock: Clock,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): HomeStore =
		object :
			HomeStore,
			Store<HomeStore.Intent, HomeComponent.State, HomeComponent.Label> by storeFactory.create(
				name = "HomeStore",
				initialState = HomeComponent.State(
					streakDays = 0,
					weekDays = emptyList(),
					wordsToReview = 0,
					estimatedMinutes = 0,
					reviewStreakDays = 0,
					trainings = emptyList(),
					monthTitle = "",
					monthDays = emptyList(),
					monthActiveDays = 0,
					monthCompletionPercent = 0,
					isCalendarVisible = false,
				),
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data class HomeLoaded(val home: Home) : Msg

		data class CalendarMonthChanged(val month: Month) : Msg

		data object ShowCalendar : Msg

		data object HideCalendar : Msg
	}

	private object ReducerImpl : Reducer<HomeComponent.State, Msg> {

		override fun HomeComponent.State.reduce(msg: Msg): HomeComponent.State =
			when (msg) {
				is Msg.HomeLoaded -> copy(
					streakDays = msg.home.streakDays,
					weekDays = msg.home.weekDays,
					wordsToReview = msg.home.wordsToReview,
					estimatedMinutes = msg.home.estimatedMinutes,
					reviewStreakDays = msg.home.reviewStreakDays,
					trainings = msg.home.trainings,
				).withMonth(msg.home.month)
				is Msg.CalendarMonthChanged -> withMonth(msg.month)
				Msg.ShowCalendar -> copy(isCalendarVisible = true)
				Msg.HideCalendar -> copy(isCalendarVisible = false)
			}

		private fun HomeComponent.State.withMonth(
			month: Month,
		): HomeComponent.State =
			copy(
				monthTitle = month.title,
				monthDays = month.days,
				monthActiveDays = month.activeDays,
				monthCompletionPercent = month.completionPercent,
			)
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			HomeStore.Intent,
			Action,
			HomeComponent.State,
			Msg,
			HomeComponent.Label,
			>() {

		private var completedDayOffsets: Set<Int> = emptySet()
		private var displayedMonth: YearMonth = YearMonth.now(clock)

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadHome()
			}
		}

		override fun executeIntent(intent: HomeStore.Intent) {
			when (intent) {
				HomeStore.Intent.OpenMonth -> {
					showCurrentMonth()
					dispatch(Msg.ShowCalendar)
				}
				HomeStore.Intent.DismissMonth -> dispatch(Msg.HideCalendar)
				HomeStore.Intent.PreviousMonth -> shiftMonth(months = -1)
				HomeStore.Intent.NextMonth -> shiftMonth(months = 1)
				HomeStore.Intent.StartReview -> publish(HomeComponent.Label.StartReview)
				is HomeStore.Intent.OpenTraining -> {
					publish(HomeComponent.Label.OpenTraining(training = intent.training))
				}
			}
		}

		private fun loadHome() {
			launchTry {
				val home = getHomeUseCase()
				completedDayOffsets = home.completedDayOffsets.toSet()
				displayedMonth = YearMonth.now(clock)
				dispatch(Msg.HomeLoaded(home = home))
			} catch {
				// Keep empty immutable state until retry is added.
			}
		}

		private fun showCurrentMonth() {
			displayedMonth = YearMonth.now(clock)
			dispatchCalendarMonth(displayedMonth)
		}

		private fun shiftMonth(months: Long) {
			displayedMonth = displayedMonth.plusMonths(months)
			dispatchCalendarMonth(displayedMonth)
		}

		private fun dispatchCalendarMonth(yearMonth: YearMonth) {
			dispatch(
				Msg.CalendarMonthChanged(
					month = getMonthUseCase(
						yearMonth = yearMonth,
						completedOffsets = completedDayOffsets,
					),
				),
			)
		}
	}
}
