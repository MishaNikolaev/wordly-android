package com.nmichail.wordly.android.features.books.data.dto

data class BookContentResponse(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val paragraphs: List<BookParagraphResponse>,
)