package com.nmichail.wordly.android.features.news.domain.entity

data class News(
	val id: String,
	val title: String,
	val subtitle: String,
	val publishedAt: String,
	val readingMinutes: Int,
	val author: String,
	val imageUrl: String?,
	val content: List<NewsContentBlock>,
)

sealed interface NewsContentBlock {

	data class Paragraph(val text: String) : NewsContentBlock

	data class Quote(val text: String) : NewsContentBlock
}
