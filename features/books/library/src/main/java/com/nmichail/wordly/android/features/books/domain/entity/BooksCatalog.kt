package com.nmichail.wordly.android.features.books.domain.entity

data class BooksCatalog(
	val title: String,
	val searchPlaceholder: String,
	val levelBanner: BooksLevelBanner?,
	val sections: List<BooksSection>,
)