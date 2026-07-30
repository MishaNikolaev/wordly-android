@file:Suppress("TooManyFunctions", "CyclomaticComplexMethod")

package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.words.add.presentation.AddWordDialogState
import com.nmichail.wordly.android.features.words.detail.presentation.RepeatDateFormatter
import com.nmichail.wordly.android.features.words.detail.presentation.WordCalendarFactory
import com.nmichail.wordly.android.features.words.detail.presentation.WordDetailDialogState
import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordLookup
import com.nmichail.wordly.android.features.words.domain.entity.WordReview
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.entity.WordTag
import com.nmichail.wordly.android.features.words.domain.entity.WordsCatalog
import com.nmichail.wordly.android.features.words.domain.entity.WordsFilters
import com.nmichail.wordly.android.features.words.domain.usecase.AddWordToReviewUseCase
import com.nmichail.wordly.android.features.words.domain.usecase.AddWordUseCase
import com.nmichail.wordly.android.features.words.domain.usecase.GetWordsUseCase
import com.nmichail.wordly.android.features.words.domain.usecase.LookupWordUseCase
import com.nmichail.wordly.android.features.words.domain.usecase.UpdateWordStatusUseCase
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.Job

internal class WordsStoreFactory @Inject constructor(
	private val getWordsUseCase: GetWordsUseCase,
	private val lookupWordUseCase: LookupWordUseCase,
	private val addWordUseCase: AddWordUseCase,
	private val updateWordStatusUseCase: UpdateWordStatusUseCase,
	private val addWordToReviewUseCase: AddWordToReviewUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): WordsStore =
		object :
			WordsStore,
			Store<WordsStore.Intent, WordsComponent.State, Nothing> by storeFactory.create(
				name = "WordsStore",
				initialState = WordsComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data object SetError : Msg

		data class CatalogLoaded(val catalog: WordsCatalog) : Msg

		data class SearchUpdated(
			val query: String,
			val words: List<WordItem>,
		) : Msg

		data class FilterUpdated(
			val filter: WordFilter,
			val words: List<WordItem>,
		) : Msg

		data class WordsUpdated(val words: List<WordItem>) : Msg

		data class AddDialogUpdated(val dialog: AddWordDialogState?) : Msg

		data class DetailDialogUpdated(val dialog: WordDetailDialogState?) : Msg
	}

	private object ReducerImpl : Reducer<WordsComponent.State, Msg> {

		override fun WordsComponent.State.reduce(msg: Msg): WordsComponent.State =
			when (msg) {
				Msg.Loading -> WordsComponent.State.Loading
				Msg.SetError -> WordsComponent.State.Error
				is Msg.CatalogLoaded -> WordsComponent.State.Content(
					title = msg.catalog.title,
					searchQuery = "",
					searchPlaceholder = msg.catalog.searchPlaceholder,
					selectedFilter = WordFilter.All,
					words = msg.catalog.words,
					tags = msg.catalog.tags,
					addWordDialog = null,
					wordDetailDialog = null,
				)
				is Msg.SearchUpdated -> {
					val content = this as? WordsComponent.State.Content ?: return this
					content.copy(
						searchQuery = msg.query,
						words = msg.words,
					)
				}
				is Msg.FilterUpdated -> {
					val content = this as? WordsComponent.State.Content ?: return this
					content.copy(
						selectedFilter = msg.filter,
						words = msg.words,
					)
				}
				is Msg.WordsUpdated -> {
					val content = this as? WordsComponent.State.Content ?: return this
					content.copy(words = msg.words)
				}
				is Msg.AddDialogUpdated -> {
					val content = this as? WordsComponent.State.Content ?: return this
					content.copy(addWordDialog = msg.dialog)
				}
				is Msg.DetailDialogUpdated -> {
					val content = this as? WordsComponent.State.Content ?: return this
					content.copy(wordDetailDialog = msg.dialog)
				}
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			WordsStore.Intent,
			Action,
			WordsComponent.State,
			Msg,
			Nothing,
			>() {

		private var tags: List<WordTag> = emptyList()
		private var lookupJob: Job? = null
		private var wordsJob: Job? = null

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadWords(showLoading = true)
			}
		}

		override fun executeIntent(intent: WordsStore.Intent) {
			when (intent) {
				WordsStore.Intent.Retry -> loadWords(showLoading = true)
				is WordsStore.Intent.ChangeSearchQuery -> {
					val content = state() as? WordsComponent.State.Content ?: return
					reloadWords(
						filters = WordsFilters(
							filter = content.selectedFilter,
							query = intent.query,
						),
						onResult = { words ->
							dispatch(
								Msg.SearchUpdated(
									query = intent.query,
									words = words,
								),
							)
						},
					)
				}
				is WordsStore.Intent.ChangeFilter -> {
					val content = state() as? WordsComponent.State.Content ?: return
					reloadWords(
						filters = WordsFilters(
							filter = intent.filter,
							query = content.searchQuery,
						),
						onResult = { words ->
							dispatch(
								Msg.FilterUpdated(
									filter = intent.filter,
									words = words,
								),
							)
						},
					)
				}
				WordsStore.Intent.OpenAddWord -> openAddWord()
				WordsStore.Intent.DismissAddWord -> {
					lookupJob?.cancel()
					dispatch(Msg.AddDialogUpdated(dialog = null))
				}
				is WordsStore.Intent.ChangeWordInput -> changeWordInput(intent.value)
				is WordsStore.Intent.ToggleTag -> toggleTag(intent.tagId)
				WordsStore.Intent.ConfirmAddWord -> confirmAddWord()
				is WordsStore.Intent.OpenWordDetail -> openWordDetail(intent.wordId)
				WordsStore.Intent.DismissWordDetail -> {
					dispatch(Msg.DetailDialogUpdated(dialog = null))
				}
				is WordsStore.Intent.ChangeDetailStatus -> changeDetailStatus(intent.status)
				is WordsStore.Intent.Calendar -> handleCalendar(intent.action)
				WordsStore.Intent.ConfirmAddToReview -> confirmAddToReview()
				WordsStore.Intent.PlayAudio -> Unit
			}
		}

		private fun currentFilters(): WordsFilters {
			val content = state() as? WordsComponent.State.Content
			return WordsFilters(
				filter = content?.selectedFilter ?: WordFilter.All,
				query = content?.searchQuery.orEmpty(),
			)
		}

		private fun loadWords(showLoading: Boolean) {
			if (showLoading) {
				dispatch(Msg.Loading)
			}
			wordsJob?.cancel()
			wordsJob = launchTry {
				val catalog = getWordsUseCase(
					filters = WordsFilters(
						filter = WordFilter.All,
						query = "",
					),
				)
				tags = catalog.tags
				dispatch(Msg.CatalogLoaded(catalog = catalog))
			} catch {
				if (showLoading) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun reloadWords(
			filters: WordsFilters,
			onResult: (List<WordItem>) -> Unit,
		) {
			wordsJob?.cancel()
			wordsJob = launchTry {
				val catalog = getWordsUseCase(filters = filters)
				tags = catalog.tags
				onResult(catalog.words)
			} catch {
				// ignored
			}
		}

		private fun refreshVisibleWords() {
			reloadWords(
				filters = currentFilters(),
				onResult = { words -> dispatch(Msg.WordsUpdated(words = words)) },
			)
		}

		private fun handleCalendar(action: WordsComponent.CalendarAction) {
			when (action) {
				WordsComponent.CalendarAction.Open -> openRepeatCalendar()
				WordsComponent.CalendarAction.Dismiss -> dismissRepeatCalendar()
				WordsComponent.CalendarAction.PreviousMonth -> shiftCalendarMonth(-1)
				WordsComponent.CalendarAction.NextMonth -> shiftCalendarMonth(1)
				WordsComponent.CalendarAction.Today -> selectToday()
				is WordsComponent.CalendarAction.DayClick -> selectCalendarDay(action.dayOfMonth)
			}
		}

		private fun openAddWord() {
			val content = state() as? WordsComponent.State.Content ?: return
			dispatch(
				Msg.AddDialogUpdated(
					dialog = AddWordDialogState(
						wordInput = "",
						phonetic = null,
						translation = null,
						definition = null,
						examples = emptyList(),
						difficulty = 1,
						selectedTagIds = emptySet(),
						availableTags = content.tags.ifEmpty { tags },
						isLookingUp = false,
						isSubmitting = false,
					),
				),
			)
		}

		private fun changeWordInput(value: String) {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.addWordDialog ?: return
			lookupJob?.cancel()
			if (value.trim().isEmpty()) {
				dispatch(
					Msg.AddDialogUpdated(
						dialog = dialog.copy(
							wordInput = value,
							phonetic = null,
							translation = null,
							definition = null,
							examples = emptyList(),
							difficulty = 1,
							isLookingUp = false,
						),
					),
				)
				return
			}
			dispatch(
				Msg.AddDialogUpdated(
					dialog = dialog.copy(
						wordInput = value,
						isLookingUp = true,
					),
				),
			)
			lookupJob = launchTry {
				val lookup = lookupWordUseCase(value)
				applyLookup(lookup = lookup, input = value)
			} catch {
				val current = (state() as? WordsComponent.State.Content)?.addWordDialog ?: return@catch
				dispatch(
					Msg.AddDialogUpdated(
						dialog = current.copy(isLookingUp = false),
					),
				)
			}
		}

		private fun applyLookup(lookup: WordLookup, input: String) {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.addWordDialog ?: return
			if (dialog.wordInput != input) return
			dispatch(
				Msg.AddDialogUpdated(
					dialog = dialog.copy(
						phonetic = lookup.phonetic,
						translation = lookup.translation,
						definition = lookup.definition,
						examples = lookup.examples,
						difficulty = lookup.difficulty,
						isLookingUp = false,
					),
				),
			)
		}

		private fun toggleTag(tagId: String) {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.addWordDialog ?: return
			val selected = dialog.selectedTagIds.toMutableSet()
			if (!selected.add(tagId)) {
				selected.remove(tagId)
			}
			dispatch(Msg.AddDialogUpdated(dialog = dialog.copy(selectedTagIds = selected)))
		}

		private fun confirmAddWord() {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.addWordDialog ?: return
			val word = dialog.wordInput.trim()
			if (word.isEmpty() || dialog.isSubmitting) return

			dispatch(Msg.AddDialogUpdated(dialog = dialog.copy(isSubmitting = true)))
			launchTry {
				addWordUseCase(
					NewWord(
						word = word,
						phonetic = dialog.phonetic,
						translation = dialog.translation,
						definition = dialog.definition,
						examples = dialog.examples,
						tagIds = dialog.selectedTagIds.toList(),
						difficulty = dialog.difficulty,
					),
				)
				refreshVisibleWords()
				dispatch(Msg.AddDialogUpdated(dialog = null))
			} catch {
				val current = (state() as? WordsComponent.State.Content)?.addWordDialog ?: return@catch
				dispatch(
					Msg.AddDialogUpdated(
						dialog = current.copy(isSubmitting = false),
					),
				)
			}
		}

		private fun openWordDetail(wordId: String) {
			val word = (state() as? WordsComponent.State.Content)?.words?.find { it.id == wordId }
				?: return
			dispatch(
				Msg.DetailDialogUpdated(
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
						isSubmittingReview = false,
						isAddedToReview = false,
					),
				),
			)
		}

		private fun changeDetailStatus(status: WordStatus) {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.wordDetailDialog ?: return
			dispatch(Msg.DetailDialogUpdated(dialog = dialog.copy(status = status)))
			launchTry {
				updateWordStatusUseCase(wordId = dialog.wordId, status = status)
				refreshVisibleWords()
			} catch {
				// ignored
			}
		}

		private fun openRepeatCalendar() {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.wordDetailDialog ?: return
			val today = LocalDate.now()
			val selected = (dialog.repeatEpochDay ?: today.toEpochDay())
				.coerceAtLeast(today.toEpochDay())
			val yearMonth = YearMonth.from(LocalDate.ofEpochDay(selected))
			dispatch(
				Msg.DetailDialogUpdated(
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
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.wordDetailDialog ?: return
			dispatch(Msg.DetailDialogUpdated(dialog = dialog.copy(calendar = null)))
		}

		private fun shiftCalendarMonth(delta: Long) {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.wordDetailDialog ?: return
			val calendar = dialog.calendar ?: return
			val next = YearMonth.of(calendar.year, calendar.month).plusMonths(delta)
			dispatch(
				Msg.DetailDialogUpdated(
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
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.wordDetailDialog ?: return
			val calendar = dialog.calendar ?: return
			val date = YearMonth.of(calendar.year, calendar.month).atDay(dayOfMonth)
			if (date.isBefore(LocalDate.now())) return
			applySelectedEpochDay(date.toEpochDay(), YearMonth.from(date))
		}

		private fun applySelectedEpochDay(epochDay: Long, yearMonth: YearMonth) {
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.wordDetailDialog ?: return
			val todayEpoch = LocalDate.now().toEpochDay()
			val safeEpochDay = epochDay.coerceAtLeast(todayEpoch)
			dispatch(
				Msg.DetailDialogUpdated(
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
			val content = state() as? WordsComponent.State.Content ?: return
			val dialog = content.wordDetailDialog ?: return
			if (dialog.isSubmittingReview || dialog.isAddedToReview) return
			val todayEpoch = LocalDate.now().toEpochDay()
			val epochDay = (dialog.repeatEpochDay ?: todayEpoch).coerceAtLeast(todayEpoch)
			dispatch(Msg.DetailDialogUpdated(dialog = dialog.copy(isSubmittingReview = true)))
			launchTry {
				addWordToReviewUseCase(
					WordReview(
						wordId = dialog.wordId,
						epochDay = epochDay,
					),
				)
				refreshVisibleWords()
				val current = (state() as? WordsComponent.State.Content)?.wordDetailDialog ?: return@launchTry
				dispatch(
					Msg.DetailDialogUpdated(
						dialog = current.copy(
							isSubmittingReview = false,
							isAddedToReview = true,
						),
					),
				)
			} catch {
				val current = (state() as? WordsComponent.State.Content)?.wordDetailDialog
					?: return@catch
				dispatch(
					Msg.DetailDialogUpdated(
						dialog = current.copy(isSubmittingReview = false),
					),
				)
			}
		}
	}
}
