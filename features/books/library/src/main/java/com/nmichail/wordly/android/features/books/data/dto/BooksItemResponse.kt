package com.nmichail.wordly.android.features.books.data.dto

data class BooksItemResponse(
    val id: String,
    val title: String,
    val subtitle: String,
    val author: String? = null,
    val genre: String? = null,
    val category: String? = null,
    val badge: String? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val readersCount: Long? = null,
)