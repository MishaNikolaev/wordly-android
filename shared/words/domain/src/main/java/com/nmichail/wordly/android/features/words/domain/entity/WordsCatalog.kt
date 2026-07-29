package com.nmichail.wordly.android.features.words.domain.entity

data class WordsCatalog(
	val title: String,
	val searchPlaceholder: String,
	val words: List<WordItem>,
	val tags: List<WordTag>,
)