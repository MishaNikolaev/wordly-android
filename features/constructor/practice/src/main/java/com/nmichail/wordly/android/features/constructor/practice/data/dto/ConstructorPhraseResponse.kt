package com.nmichail.wordly.android.features.constructor.practice.data.dto

data class ConstructorPhraseResponse(
    val id: String,
    val question: String,
    val author: String?,
    val words: List<ConstructorWordResponse>,
    val correctOrder: List<String>,
)