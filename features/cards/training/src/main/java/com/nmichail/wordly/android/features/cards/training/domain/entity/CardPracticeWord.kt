package com.nmichail.wordly.android.features.cards.training.domain.entity

data class CardPracticeWord(
	val id: String,
	val word: String,
	val phonetic: String,
	val audioUrl: String?,
	val options: List<CardPracticeOption>,
	val correctOptionId: String,
)
