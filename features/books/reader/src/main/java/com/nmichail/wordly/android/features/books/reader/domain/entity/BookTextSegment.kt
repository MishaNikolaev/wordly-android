package com.nmichail.wordly.android.features.books.reader.domain.entity

data class BookTextSegment(
    val type: BookTextSegmentType,
    val text: String,
    val id: String?,
    val definition: BookWordDefinition?,
)