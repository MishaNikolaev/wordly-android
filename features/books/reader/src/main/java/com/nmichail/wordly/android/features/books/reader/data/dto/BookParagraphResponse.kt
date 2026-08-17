package com.nmichail.wordly.android.features.books.reader.data.dto

data class BookParagraphResponse(
    val id: String,
    val segments: List<BookTextSegmentResponse>,
)