package com.nmichail.wordly.android.features.books.data.dto

data class BooksSectionResponse(
    val title: String,
    val items: List<BooksItemResponse>,
)