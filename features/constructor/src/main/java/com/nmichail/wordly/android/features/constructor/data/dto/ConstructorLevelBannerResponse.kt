package com.nmichail.wordly.android.features.constructor.data.dto

data class ConstructorLevelBannerResponse(
	val text: String,
	val levelLabel: String,
	val levels: List<String> = emptyList(),
)