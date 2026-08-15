package com.nmichail.wordly.android.features.words.presentation.list

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordTag

interface WordsListStore :
	Store<WordsListStore.Intent, WordsListStore.State, Nothing> {

	sealed interface State {

		data object Loading : State

		data class Content(
			val title: String,
			val searchQuery: String,
			val searchPlaceholder: String,
			val selectedFilter: WordFilter,
			val words: List<WordItem>,
			val tags: List<WordTag>,
		) : State

		data object Error : State
	}

	sealed interface Intent {

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class ChangeFilter(val filter: WordFilter) : Intent

		data object Refresh : Intent
	}
}