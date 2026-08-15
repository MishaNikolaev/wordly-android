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
import com.nmichail.wordly.android.features.home.domain.entity.TrainingType
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
			Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> by storeFactory.create(
				name = "HomeStore",
				initialState = HomeStore.State.Loading,
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
			val completedDayOffsets: Set<Int>,
			val displayedMonth: YearMonth,
			val month: Month,
		) : Msg

		data class CalendarMonthChanged(
			val displayedMonth: YearMonth,
			val month: Month,
		) : Msg

		data object ShowCalendar : Msg

		data object HideCalendar : Msg

		data object SetError : Msg
	}

	private object ReducerImpl : Reducer<HomeStore.State, Msg> {

		override fun HomeStore.State.reduce(msg: Msg): HomeStore.State {
			val content = this as? HomeStore.State.Content
			return when (msg) {
				Msg.Loading -> HomeStore.State.Loading
				is Msg.HomeLoaded -> HomeStore.State.Content(
					firstName = msg.home.firstName,
					streakDays = msg.home.streakDays,
					weekDays = msg.weekDays,
					wordsToReview = msg.home.wordsToReview,
					estimatedMinutes = msg.home.estimatedMinutes,
					reviewStreakDays = msg.home.reviewStreakDays,
					trainings = msg.home.trainings,
					completedDayOffsets = msg.completedDayOffsets,
					displayedMonth = msg.displayedMonth,
					monthTitle = msg.month.title,
					monthDays = msg.month.days,
					monthActiveDays = msg.month.activeDays,
					monthCompletionPercent = msg.month.completionPercent,
					calendarVisible = false,
				)
				is Msg.CalendarMonthChanged -> content?.copy(
					displayedMonth = msg.displayedMonth,
					monthTitle = msg.month.title,
					monthDays = msg.month.days,
					monthActiveDays = msg.month.activeDays,
					monthCompletionPercent = msg.month.completionPercent,
				) ?: this
				Msg.ShowCalendar -> content?.copy(calendarVisible = true) ?: this
				Msg.HideCalendar -> content?.copy(calendarVisible = false) ?: this
				Msg.SetError -> HomeStore.State.Error
			}
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
				HomeStore.Intent.StartReview -> publish(HomeStore.Label.StartReview)
				is HomeStore.Intent.OpenTraining -> when (intent.training.type) {
					TrainingType.Cards -> publish(HomeStore.Label.OpenCards)
					TrainingType.Constructor -> publish(HomeStore.Label.OpenConstructor)
					TrainingType.Books -> publish(HomeStore.Label.OpenBooks)
				}
			}
		}

		private fun loadHome() {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					val home = getHomeUseCase()
					val completedDayOffsets = home.completedDayOffsets.toSet()
					val displayedMonth = YearMonth.now(clock)
					dispatch(
						Msg.HomeLoaded(
							home = home,
							weekDays = weekDaysFactory(completedOffsets = completedDayOffsets),
							completedDayOffsets = completedDayOffsets,
							displayedMonth = displayedMonth,
							month = monthFactory(
								yearMonth = displayedMonth,
								completedOffsets = completedDayOffsets,
							),
						),
					)
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun showCurrentMonth() {
			val content = state() as? HomeStore.State.Content ?: return
			val displayedMonth = YearMonth.now(clock)
			dispatchCalendarMonth(
				displayedMonth = displayedMonth,
				completedDayOffsets = content.completedDayOffsets,
			)
		}

		private fun shiftMonth(months: Long) {
			val content = state() as? HomeStore.State.Content ?: return
			dispatchCalendarMonth(
				displayedMonth = content.displayedMonth.plusMonths(months),
				completedDayOffsets = content.completedDayOffsets,
			)
		}

		private fun dispatchCalendarMonth(
			displayedMonth: YearMonth,
			completedDayOffsets: Set<Int>,
		) {
			dispatch(
				Msg.CalendarMonthChanged(
					displayedMonth = displayedMonth,
					month = monthFactory(
						yearMonth = displayedMonth,
						completedOffsets = completedDayOffsets,
					),
				),
			)
		}
	}
}