package com.nmichail.wordly.android.features.words.data.dto

data class WordsCatalogDto(
	val title: String,
	val searchPlaceholder: String,
	val words: List<WordItemDto>,
	val tags: List<WordTagDto>,
)