package com.nmichail.wordly.android.features.home.data.dto

data class NewsResponse(
	val id: String,
	val title: String,
	val subtitle: String,
	val publishedAt: String,
	val readingMinutes: Int = 4,
	val author: String = "Редакция Wordly",
	val imageUrl: String? = null,
)
