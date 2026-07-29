package com.nmichail.wordly.android.features.words.data.dto

data class WordItemDto(
	val id: String,
	val word: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val status: String,
	val tags: List<String>,
	val examples: List<WordExampleDto>,
	val difficulty: Int,
	val repeatEpochDay: Long?,
)