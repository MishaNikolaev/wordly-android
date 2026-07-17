package com.nmichail.wordly.android.features.review.domain.entity

data class ReviewQuestion(
	val word: String,
	val phonetic: String,
	val taskLabel: String,
	val options: List<ReviewOption>,
	val currentIndex: Int,
	val totalCount: Int,
)

data class ReviewOption(
	val id: String,
	val text: String,
)
