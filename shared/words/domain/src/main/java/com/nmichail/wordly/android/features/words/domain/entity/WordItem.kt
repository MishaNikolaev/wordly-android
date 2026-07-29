package com.nmichail.wordly.android.features.words.domain.entity

data class WordItem(
	val id: String,
	val word: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val status: WordStatus,
	val tags: List<String>,
	val examples: List<WordExample>,
	val difficulty: Int,
	val repeatEpochDay: Long?,
)