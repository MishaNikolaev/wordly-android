package com.nmichail.wordly.android.features.cards.domain.entity

data class Cards(
	val title: String,
	val searchPlaceholder: String,
	val levelBanner: CardsLevelBanner?,
	val sections: List<CardsSection>,
)