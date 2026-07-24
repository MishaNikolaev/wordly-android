package com.nmichail.wordly.android.features.review.domain.entity

data class ReviewWord(
	val id: String,
	val word: String,
	val phonetic: String,
	val audioUrl: String?,
	val options: List<ReviewOption>,
	val correctOptionId: String,
)
