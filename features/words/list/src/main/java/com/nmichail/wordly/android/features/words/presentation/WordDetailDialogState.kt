package com.nmichail.wordly.android.features.words.presentation

import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.shared.calendar.CalendarMonth

data class WordDetailDialogState(
	val wordId: String,
	val word: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val examples: List<WordExample>,
	val status: WordStatus,
	val tags: List<String>,
	val difficulty: Int,
	val maxDifficulty: Int,
	val repeatEpochDay: Long?,
	val repeatDateLabel: String,
	val calendar: CalendarMonth?,
	val submittingReview: Boolean,
	val addedToReview: Boolean,
)
