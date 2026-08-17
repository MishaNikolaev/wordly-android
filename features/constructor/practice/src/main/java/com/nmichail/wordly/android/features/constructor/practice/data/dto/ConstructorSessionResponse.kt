package com.nmichail.wordly.android.features.constructor.practice.data.dto

data class ConstructorSessionResponse(
    val themeId: String,
    val themeTitle: String,
    val phrases: List<ConstructorPhraseResponse>,
)