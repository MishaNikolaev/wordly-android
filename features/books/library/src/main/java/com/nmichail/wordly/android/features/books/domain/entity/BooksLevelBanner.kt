package com.nmichail.wordly.android.features.books.domain.entity

data class BooksLevelBanner(
    val text: String,
    val levelLabel: String,
    val levels: List<String>,
)