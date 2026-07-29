package com.nmichail.wordly.android.features.words.presentation

import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus

internal fun filterWords(
	words: List<WordItem>,
	query: String,
	wordFilter: WordFilter,
): List<WordItem> {
	val status = when (wordFilter) {
		WordFilter.All -> null
		WordFilter.New -> WordStatus.New
		WordFilter.InProgress -> WordStatus.InProgress
		WordFilter.Learned -> WordStatus.Learned
	}
	val normalized = query.trim()
	return words.filter { item ->
		val matchesFilter = status == null || item.status == status
		val matchesQuery = normalized.isEmpty() ||
			item.word.contains(normalized, ignoreCase = true) ||
			item.translation.orEmpty().contains(normalized, ignoreCase = true) ||
			item.definition.orEmpty().contains(normalized, ignoreCase = true) ||
			item.phonetic.orEmpty().contains(normalized, ignoreCase = true)
		matchesFilter && matchesQuery
	}
}