package com.nmichail.wordly.android.features.books.data.dto

data class BookTextSegmentResponse(
    val type: String,
    val text: String,
    val id: String? = null,
    val definition: BookWordDefinitionResponse? = null,
)