package com.nmichail.wordly.android.features.cards.domain.entity

data class CardsItem(
	val id: String,
	val title: String,
	val subtitle: String,
	val badge: String?,
	val imageUrl: String?,
)