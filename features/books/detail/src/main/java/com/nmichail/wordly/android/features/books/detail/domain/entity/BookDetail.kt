package com.nmichail.wordly.android.features.books.detail.domain.entity

data class BookDetail(
	val id: String,
	val title: String,
	val author: String,
	val coverUrl: String?,
	val description: String,
	val genre: String? = null,
	val category: String? = null,
	val level: String? = null,
)