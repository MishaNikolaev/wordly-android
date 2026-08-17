package com.nmichail.wordly.android.features.books.reader.domain.entity

data class BookWordDefinition(
    val word: String,
    val phonetic: String?,
    val translation: String,
    val partOfSpeech: String?,
    val example: String?,
)