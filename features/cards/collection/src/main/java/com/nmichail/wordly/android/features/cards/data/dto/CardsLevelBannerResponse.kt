package com.nmichail.wordly.android.features.cards.data.dto

data class CardsLevelBannerResponse(
	val text: String,
	val levelLabel: String,
	val levels: List<String> = emptyList(),
)