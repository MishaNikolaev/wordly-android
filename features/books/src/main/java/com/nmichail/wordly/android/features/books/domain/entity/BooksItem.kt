package com.nmichail.wordly.android.features.books.domain.entity

data class BooksItem(
	val id: String,
	val title: String,
	val subtitle: String,
	val badge: String?,
	val imageUrl: String?,
)