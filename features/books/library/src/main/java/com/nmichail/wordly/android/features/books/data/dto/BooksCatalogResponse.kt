package com.nmichail.wordly.android.features.books.data.dto

data class BooksCatalogResponse(
	val title: String,
	val searchPlaceholder: String,
	val levelBanner: BooksLevelBannerResponse? = null,
	val sections: List<BooksSectionResponse>,
)