package com.nmichail.wordly.android.features.books.data.dto

data class BooksLevelBannerResponse(
	val text: String,
	val levelLabel: String,
	val levels: List<String> = emptyList(),
)