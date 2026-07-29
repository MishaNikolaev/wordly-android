package com.nmichail.wordly.android.features.words.domain.entity

data class WordLookup(
	val word: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val examples: List<WordExample>,
	val difficulty: Int,
)