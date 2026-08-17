package com.nmichail.wordly.android.features.books.domain.entity

data class BookParagraph(
    val id: String,
    val segments: List<BookTextSegment>,
)