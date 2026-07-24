package com.nmichail.wordly.android.features.review.data.dto

data class ReviewSessionResponse(
	val words: List<ReviewWordResponse>,
)

data class ReviewWordResponse(
	val id: String,
	val word: String,
	val phonetic: String,
	val audioUrl: String?,
	val options: List<ReviewOptionResponse>,
	val correctOptionId: String,
)

data class ReviewOptionResponse(
	val id: String,
	val text: String,
)

data class ReviewAnswerRequest(
	val wordId: String,
	val selectedOptionId: String,
	val correct: Boolean,
)