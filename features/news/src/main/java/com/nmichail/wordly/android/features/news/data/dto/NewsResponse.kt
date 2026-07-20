package com.nmichail.wordly.android.features.news.data.dto

data class NewsResponse(
	val id: String,
	val title: String,
	val subtitle: String,
	val publishedAt: String,
	val readingMinutes: Int,
	val author: String,
	val imageUrl: String?,
	val content: List<NewsContentBlockResponse>,
)

data class NewsContentBlockResponse(
	val type: String,
	val text: String,
)