package com.nmichail.wordly.android.features.books.detail.data.dto

data class BookDetailResponse(
	val id: String,
	val title: String,
	val author: String,
	val coverUrl: String? = null,
	val description: String? = null,
	val genre: String? = null,
	val category: String? = null,
	val badge: String? = null,
)