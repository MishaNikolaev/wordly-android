package com.nmichail.wordly.android.features.words.data.dto

data class AddWordBody(
	val word: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val examples: List<WordExampleDto>,
	val tagIds: List<String>,
	val difficulty: Int,
)