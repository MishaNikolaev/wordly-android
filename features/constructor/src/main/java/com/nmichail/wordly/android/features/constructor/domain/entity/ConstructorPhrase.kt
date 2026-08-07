package com.nmichail.wordly.android.features.constructor.domain.entity

data class ConstructorPhrase(
	val id: String,
	val question: String,
	val author: String?,
	val words: List<ConstructorWord>,
	val correctOrder: List<String>,
)