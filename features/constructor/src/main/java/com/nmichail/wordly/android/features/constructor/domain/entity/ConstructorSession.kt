package com.nmichail.wordly.android.features.constructor.domain.entity

data class ConstructorSession(
	val themeId: String,
	val themeTitle: String,
	val phrases: List<ConstructorPhrase>,
)
