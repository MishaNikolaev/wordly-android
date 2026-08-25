package com.nmichail.wordly.android.features.books.domain.entity

data class BooksItem(
    val id: String,
    val title: String,
    val author: String,
    val genre: String?,
    val category: String?,
    val badge: String?,
    val imageUrl: String?,
    val description: String,
    val readersCount: Long?,
)