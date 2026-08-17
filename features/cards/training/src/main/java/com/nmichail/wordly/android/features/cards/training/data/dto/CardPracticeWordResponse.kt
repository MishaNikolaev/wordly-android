package com.nmichail.wordly.android.features.cards.training.data.dto

data class CardPracticeWordResponse(
	val id: String,
	val word: String,
	val phonetic: String,
	val audioUrl: String?,
	val options: List<CardPracticeOptionResponse>,
	val correctOptionId: String,
)
