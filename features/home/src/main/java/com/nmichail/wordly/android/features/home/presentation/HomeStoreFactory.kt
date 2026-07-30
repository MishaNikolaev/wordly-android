package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.usecase.GetHomeUseCase
import com.nmichail.wordly.android.features.home.presentation.calendar.Month
import com.nmichail.wordly.android.features.home.presentation.calendar.MonthFactory
import com.nmichail.wordly.android.features.home.presentation.calendar.WeekDay
import com.nmichail.wordly.android.features.home.presentation.calendar.WeekDaysFactory
import java.time.Clock
import java.time.YearMonth
import javax.inject.Inject

internal class HomeStoreFactory @Inject constructor(
	private val getHomeUseCase: GetHomeUseCase,
	private val weekDaysFactory: WeekDaysFactory,
	private val monthFactory: MonthFactory,
	private val clock: Clock,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): HomeStore =
		object :
			HomeStore,
			Store<HomeStore.Intent, HomeComponent.State, HomeComponent.Label> by storeFactory.create(
				name = "HomeStore",
				initialState = HomeComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class HomeLoaded(
			val home: Home,
			val weekDays: List<WeekDay>,
			val month: Month,
		) : Msg

		data class CalendarMonthChanged(val month: Month) : Msg

		data object ShowCalendar : Msg

		data object HideCalendar : Msg

		data object SetError : Msg
	}

	private object ReducerImpl : Reducer<HomeComponent.State, Msg> {

		override fun HomeComponent.State.reduce(msg: Msg): HomeComponent.State =
			when (msg) {
				Msg.Loading -> HomeComponent.State.Loading
				is Msg.HomeLoaded -> HomeComponent.State.Content(
					firstName = msg.home.firstName,
					streakDays = msg.home.streakDays,
					weekDays = msg.weekDays,
					wordsToReview = msg.home.wordsToReview,
					estimatedMinutes = msg.home.estimatedMinutes,
					reviewStreakDays = msg.home.reviewStreakDays,
					trainings = msg.home.trainings,
					monthTitle = msg.month.title,
					monthDays = msg.month.days,
					monthActiveDays = msg.month.activeDays,
					monthCompletionPercent = msg.month.completionPercent,
					isCalendarVisible = false,
				)
				is Msg.CalendarMonthChanged -> when (this) {
					is HomeComponent.State.Content -> withMonth(msg.month)
					HomeComponent.State.Loading,
					HomeComponent.State.Error,
					-> this
				}
				Msg.ShowCalendar -> when (this) {
					is HomeComponent.State.Content -> copy(isCalendarVisible = true)
					HomeComponent.State.Loading,
					HomeComponent.State.Error,
					-> this
				}
				Msg.HideCalendar -> when (this) {
					is HomeComponent.State.Content -> copy(isCalendarVisible = false)
					HomeComponent.State.Loading,
					HomeComponent.State.Error,
					-> this
				}
				Msg.SetError -> HomeComponent.State.Error
			}

		private fun HomeComponent.State.Content.withMonth(
			month: Month,
		): HomeComponent.State.Content =
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
				HomeStore.Intent.Retry -> loadHome()
				HomeStore.Intent.OpenMonth -> {
					showCurrentMonth()
					dispatch(Msg.ShowCalendar)
				}
				HomeStore.Intent.DismissMonth -> dispatch(Msg.HideCalendar)
				HomeStore.Intent.PreviousMonth -> shiftMonth(months = -1)
				HomeStore.Intent.NextMonth -> shiftMonth(months = 1)
				HomeStore.Intent.GoToCurrentMonth -> showCurrentMonth()
				HomeStore.Intent.StartReview -> publish(HomeComponent.Label.StartReview)
				is HomeStore.Intent.OpenTraining -> {
					publish(HomeComponent.Label.OpenTraining(training = intent.training))
				}
			}
		}

		private fun loadHome() {
			dispatch(Msg.Loading)
			launchTry {
				val home = getHomeUseCase()
				completedDayOffsets = home.completedDayOffsets.toSet()
				displayedMonth = YearMonth.now(clock)
				dispatch(
					Msg.HomeLoaded(
						home = home,
						weekDays = weekDaysFactory(completedOffsets = completedDayOffsets),
						month = monthFactory(
							yearMonth = displayedMonth,
							completedOffsets = completedDayOffsets,
						),
					),
				)
			} catch {
				dispatch(Msg.SetError)
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
					month = monthFactory(
						yearMonth = yearMonth,
						completedOffsets = completedDayOffsets,
					),
				),
			)
		}
	}
}
