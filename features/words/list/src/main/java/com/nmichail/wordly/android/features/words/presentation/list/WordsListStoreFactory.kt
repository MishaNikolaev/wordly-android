package com.nmichail.wordly.android.features.words.presentation.list

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordsCatalog
import com.nmichail.wordly.android.features.words.domain.entity.WordsFilters
import com.nmichail.wordly.android.features.words.domain.usecase.GetWordsUseCase
import javax.inject.Inject
import kotlinx.coroutines.Job

internal class WordsListStoreFactory @Inject constructor(
	private val getWordsUseCase: GetWordsUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): WordsListStore =
		object :
			WordsListStore,
			Store<WordsListStore.Intent, WordsListStore.State, Nothing> by storeFactory.create(
				name = "WordsListStore",
				initialState = WordsListStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Init : Action
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
	}

	private object ReducerImpl : Reducer<WordsListStore.State, Msg> {

		override fun WordsListStore.State.reduce(msg: Msg): WordsListStore.State =
			when (msg) {
				Msg.Loading -> WordsListStore.State.Loading
				Msg.SetError -> WordsListStore.State.Error
				is Msg.CatalogLoaded -> WordsListStore.State.Content(
					title = msg.catalog.title,
					searchQuery = "",
					searchPlaceholder = msg.catalog.searchPlaceholder,
					selectedFilter = WordFilter.All,
					words = msg.catalog.words,
					tags = msg.catalog.tags,
				)
				is Msg.SearchUpdated -> {
					val content = this as? WordsListStore.State.Content ?: return this
					content.copy(
						searchQuery = msg.query,
						words = msg.words,
					)
				}
				is Msg.FilterUpdated -> {
					val content = this as? WordsListStore.State.Content ?: return this
					content.copy(
						selectedFilter = msg.filter,
						words = msg.words,
					)
				}
				is Msg.WordsUpdated -> {
					val content = this as? WordsListStore.State.Content ?: return this
					content.copy(words = msg.words)
				}
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			WordsListStore.Intent,
			Action,
			WordsListStore.State,
			Msg,
			Nothing,
			>() {

		private var wordsJob: Job? = null

		override fun executeAction(action: Action) {
			when (action) {
				Action.Init -> loadWords(showLoading = true)
			}
		}

		override fun executeIntent(intent: WordsListStore.Intent) {
			when (intent) {
				WordsListStore.Intent.Retry -> loadWords(showLoading = true)
				WordsListStore.Intent.Refresh -> refreshVisibleWords()
				is WordsListStore.Intent.ChangeSearchQuery -> {
					val content = state() as? WordsListStore.State.Content ?: return
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
				is WordsListStore.Intent.ChangeFilter -> {
					val content = state() as? WordsListStore.State.Content ?: return
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
			}
		}

		private fun currentFilters(): WordsFilters {
			val content = state() as? WordsListStore.State.Content
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
			wordsJob = scope.launch {
				try {
					val catalog = getWordsUseCase(
						filters = WordsFilters(
							filter = WordFilter.All,
							query = "",
						),
					)
					dispatch(Msg.CatalogLoaded(catalog = catalog))
				} catch (_: Exception) {
					if (showLoading) {
						dispatch(Msg.SetError)
					}
				}
			}
		}

		private fun reloadWords(
			filters: WordsFilters,
			onResult: (List<WordItem>) -> Unit,
		) {
			wordsJob?.cancel()
			wordsJob = scope.launch {
				val catalog = getWordsUseCase(filters = filters)
				onResult(catalog.words)
			}
		}

		private fun refreshVisibleWords() {
			reloadWords(
				filters = currentFilters(),
				onResult = { words -> dispatch(Msg.WordsUpdated(words = words)) },
			)
		}
	}
}