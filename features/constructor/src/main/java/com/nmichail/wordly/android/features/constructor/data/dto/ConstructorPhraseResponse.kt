package com.nmichail.wordly.android.features.constructor.data.dto

data class ConstructorPhraseResponse(
	val id: String,
	val prompt: String,
	val author: String?,
	val words: List<ConstructorWordResponse>,
	val correctOrder: List<String>,
)