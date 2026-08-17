package com.nmichail.wordly.android.features.books.domain.entity

data class BooksSection(
    val title: String,
    val items: List<BooksItem>,
)