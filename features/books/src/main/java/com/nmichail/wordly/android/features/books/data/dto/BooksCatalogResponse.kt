package com.nmichail.wordly.android.features.books.data.dto

data class BooksCatalogResponse(
	val title: String,
	val searchPlaceholder: String,
	val levelBanner: BooksLevelBannerResponse?,
	val sections: List<BooksSectionResponse>,
)