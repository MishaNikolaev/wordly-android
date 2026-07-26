package com.nmichail.wordly.android.features.books.data.dto

data class BooksItemResponse(
	val id: String,
	val title: String,
	val subtitle: String,
	val badge: String?,
	val imageUrl: String?,
)