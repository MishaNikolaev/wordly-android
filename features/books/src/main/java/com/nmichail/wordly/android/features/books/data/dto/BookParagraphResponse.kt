package com.nmichail.wordly.android.features.books.data.dto

data class BookParagraphResponse(
	val id: String,
	val segments: List<BookTextSegmentResponse>,
)