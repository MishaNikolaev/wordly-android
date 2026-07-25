package com.nmichail.wordly.android.features.cards.data.dto

data class CardsItemResponse(
	val id: String,
	val title: String,
	val subtitle: String,
	val badge: String?,
	val imageUrl: String?,
)