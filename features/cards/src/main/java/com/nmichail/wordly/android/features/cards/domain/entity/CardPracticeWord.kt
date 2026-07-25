package com.nmichail.wordly.android.features.cards.domain.entity

data class CardPracticeWord(
	val id: String,
	val word: String,
	val phonetic: String,
	val audioUrl: String?,
	val options: List<CardPracticeOption>,
	val correctOptionId: String,
)
