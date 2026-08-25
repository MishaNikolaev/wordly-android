package com.nmichail.wordly.android.features.words.data.dto

data class VocabularyLookupDto(
	val word: String,
	val phonetic: String?,
	val translation: String?,
	val definition: String?,
	val audioUrl: String?,
	val examples: List<VocabularyExampleDto>?,
	val level: String?,
	val difficulty: Int?,
	val pos: String?,
)

data class VocabularyExampleDto(
	val text: String,
	val translation: String?,
)
