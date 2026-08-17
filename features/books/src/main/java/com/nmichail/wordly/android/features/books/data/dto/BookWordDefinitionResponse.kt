package com.nmichail.wordly.android.features.books.data.dto

data class BookWordDefinitionResponse(
    val word: String,
    val phonetic: String?,
    val translation: String,
    val partOfSpeech: String?,
    val example: String?,
)