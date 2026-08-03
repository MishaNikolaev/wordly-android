package com.nmichail.wordly.android.features.words.detail.presentation

import com.nmichail.wordly.android.component.ui.components.calendar.CalendarDay
import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus

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
	val calendar: CalendarState?,
	val isSubmittingReview: Boolean,
	val isAddedToReview: Boolean,
)

data class CalendarState(
	val monthTitle: String,
	val year: Int,
	val month: Int,
	val days: List<CalendarDay?>,
	val selectedEpochDay: Long,
)