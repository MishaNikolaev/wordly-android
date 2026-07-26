package com.nmichail.wordly.android.features.books.domain.entity

data class BookContent(
	val id: String,
	val title: String,
	val author: String,
	val coverUrl: String?,
	val paragraphs: List<BookParagraph>,
)