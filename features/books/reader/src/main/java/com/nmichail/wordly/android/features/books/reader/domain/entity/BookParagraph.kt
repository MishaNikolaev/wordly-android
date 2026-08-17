package com.nmichail.wordly.android.features.books.reader.domain.entity

data class BookParagraph(
    val id: String,
    val segments: List<BookTextSegment>,
)