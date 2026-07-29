package com.nmichail.wordly.android.features.words.domain.entity

data class NewWord(
	val word: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val examples: List<WordExample>,
	val tagIds: List<String>,
	val difficulty: Int,
)