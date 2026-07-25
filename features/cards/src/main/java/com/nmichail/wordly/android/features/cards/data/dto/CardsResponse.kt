package com.nmichail.wordly.android.features.cards.data.dto

data class CardsResponse(
	val title: String,
	val searchPlaceholder: String,
	val levelBanner: CardsLevelBannerResponse?,
	val sections: List<CardsSectionResponse>,
)