@file:Suppress("TooManyFunctions")

package com.nmichail.wordly.android.features.words.presentation.detail

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordReview
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.usecase.AddWordToReviewUseCase
import com.nmichail.wordly.android.features.words.domain.usecase.UpdateWordStatusUseCase
import com.nmichail.wordly.android.features.words.presentation.RepeatDateFormatter
import com.nmichail.wordly.android.features.words.presentation.WordCalendarFactory
import com.nmichail.wordly.android.features.words.presentation.WordDetailDialogState
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

internal class WordDetailStoreFactory @Inject constructor(
	private val updateWordStatusUseCase: UpdateWordStatusUseCase,
	private val addWordToReviewUseCase: AddWordToReviewUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): WordDetailStore =
		object :
			WordDetailStore,
			Store<WordDetailStore.Intent, WordDetailStore.State, WordDetailStore.Label>
			by storeFactory.create(
				name = "WordDetailStore",
				initialState = WordDetailStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Init : Action
	}

	private sealed interface Msg {

		data class Opened(val dialog: WordDetailDialogState) : Msg

		data object Closed : Msg

		data class DialogUpdated(val dialog: WordDetailDialogState) : Msg
	}

	private object ReducerImpl : Reducer<WordDetailStore.State, Msg> {

		override fun WordDetailStore.State.reduce(msg: Msg): WordDetailStore.State =
			when (msg) {
				is Msg.Opened -> WordDetailStore.State.Open(dialog = msg.dialog)
				Msg.Closed -> WordDetailStore.State.Closed
				is Msg.DialogUpdated -> {
					val open = this as? WordDetailStore.State.Open ?: return this
					open.copy(dialog = msg.dialog)
				}
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			WordDetailStore.Intent,
			Action,
			WordDetailStore.State,
			Msg,
			WordDetailStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Init -> dispatch(Msg.Closed)
			}
		}

		override fun executeIntent(intent: WordDetailStore.Intent) {
			when (intent) {
				is WordDetailStore.Intent.Open -> open(word = intent.word)
				WordDetailStore.Intent.Dismiss -> {
					dispatch(Msg.Closed)
					publish(WordDetailStore.Label.Dismiss)
				}
				is WordDetailStore.Intent.ChangeStatus -> changeStatus(intent.status)
				is WordDetailStore.Intent.Calendar -> handleCalendar(intent.action)
				WordDetailStore.Intent.ConfirmAddToReview -> confirmAddToReview()
				WordDetailStore.Intent.PlayAudio -> Unit
			}
		}

		private fun currentDialog(): WordDetailDialogState? =
			(state() as? WordDetailStore.State.Open)?.dialog

		private fun open(word: WordItem) {
			dispatch(
				Msg.Opened(
					dialog = WordDetailDialogState(
                        wordId = word.id,
                        word = word.word,
                        phonetic = word.phonetic,
                        translation = word.translation,
                        definition = word.definition,
                        examples = word.examples,
                        status = word.status,
                        tags = word.tags,
                        difficulty = word.difficulty,
                        maxDifficulty = 5,
                        repeatEpochDay = word.repeatEpochDay,
                        repeatDateLabel = RepeatDateFormatter.label(word.repeatEpochDay),
                        calendar = null,
                        submittingReview = false,
                        addedToReview = false,
                    ),
				),
			)
		}

		private fun changeStatus(status: WordStatus) {
			val dialog = currentDialog() ?: return
			dispatch(Msg.DialogUpdated(dialog = dialog.copy(status = status)))
			scope.launch {
				updateWordStatusUseCase(wordId = dialog.wordId, status = status)
				publish(WordDetailStore.Label.Changed)
			}
		}

		private fun handleCalendar(action: WordDetailStore.CalendarAction) {
			when (action) {
				WordDetailStore.CalendarAction.Open -> openRepeatCalendar()
				WordDetailStore.CalendarAction.Dismiss -> dismissRepeatCalendar()
				WordDetailStore.CalendarAction.PreviousMonth -> changeDisplayedMonth(months = -1)
				WordDetailStore.CalendarAction.NextMonth -> changeDisplayedMonth(months = 1)
				WordDetailStore.CalendarAction.Today -> selectToday()
				is WordDetailStore.CalendarAction.DayClick -> selectCalendarDay(action.dayOfMonth)
			}
		}

		private fun openRepeatCalendar() {
			val dialog = currentDialog() ?: return
			val today = LocalDate.now()
			val selected = (dialog.repeatEpochDay ?: today.toEpochDay())
				.coerceAtLeast(today.toEpochDay())
			val yearMonth = YearMonth.from(LocalDate.ofEpochDay(selected))
			dispatch(
				Msg.DialogUpdated(
					dialog = dialog.copy(
						repeatEpochDay = selected,
						repeatDateLabel = RepeatDateFormatter.label(selected),
						calendar = WordCalendarFactory.build(
							yearMonth = yearMonth,
							selectedEpochDay = selected,
						),
					),
				),
			)
		}

		private fun dismissRepeatCalendar() {
			val dialog = currentDialog() ?: return
			dispatch(Msg.DialogUpdated(dialog = dialog.copy(calendar = null)))
		}

		private fun changeDisplayedMonth(months: Long) {
			val dialog = currentDialog() ?: return
			val calendar = dialog.calendar ?: return
			val next = YearMonth.of(calendar.year, calendar.month).plusMonths(months)
			dispatch(
				Msg.DialogUpdated(
					dialog = dialog.copy(
						calendar = WordCalendarFactory.build(
							yearMonth = next,
							selectedEpochDay = calendar.selectedEpochDay,
						),
					),
				),
			)
		}

		private fun selectToday() {
			val today = LocalDate.now()
			applySelectedEpochDay(today.toEpochDay(), YearMonth.from(today))
		}

		private fun selectCalendarDay(dayOfMonth: Int) {
			val dialog = currentDialog() ?: return
			val calendar = dialog.calendar ?: return
			val date = YearMonth.of(calendar.year, calendar.month).atDay(dayOfMonth)
			if (date.isBefore(LocalDate.now())) return
			applySelectedEpochDay(date.toEpochDay(), YearMonth.from(date))
		}

		private fun applySelectedEpochDay(epochDay: Long, yearMonth: YearMonth) {
			val dialog = currentDialog() ?: return
			val todayEpoch = LocalDate.now().toEpochDay()
			val safeEpochDay = epochDay.coerceAtLeast(todayEpoch)
			dispatch(
				Msg.DialogUpdated(
					dialog = dialog.copy(
						repeatEpochDay = safeEpochDay,
						repeatDateLabel = RepeatDateFormatter.label(safeEpochDay),
						calendar = WordCalendarFactory.build(
							yearMonth = yearMonth,
							selectedEpochDay = safeEpochDay,
						),
					),
				),
			)
		}

		private fun confirmAddToReview() {
			val dialog = currentDialog() ?: return
			if (dialog.submittingReview || dialog.addedToReview) return
			val todayEpoch = LocalDate.now().toEpochDay()
			val epochDay = (dialog.repeatEpochDay ?: todayEpoch).coerceAtLeast(todayEpoch)
			dispatch(Msg.DialogUpdated(dialog = dialog.copy(submittingReview = true)))
			scope.launch {
				try {
					addWordToReviewUseCase(
						WordReview(
							wordId = dialog.wordId,
							epochDay = epochDay,
						),
					)
					val current = currentDialog() ?: return@launch
					dispatch(
						Msg.DialogUpdated(
							dialog = current.copy(
								submittingReview = false,
								addedToReview = true,
							),
						),
					)
					publish(WordDetailStore.Label.Changed)
				} catch (_: Exception) {
					val current = currentDialog() ?: return@launch
					dispatch(Msg.DialogUpdated(dialog = current.copy(submittingReview = false)))
				}
			}
		}
	}
}
