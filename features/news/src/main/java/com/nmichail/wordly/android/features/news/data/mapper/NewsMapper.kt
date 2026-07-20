package com.nmichail.wordly.android.features.news.data.mapper

import com.nmichail.wordly.android.features.news.data.dto.NewsContentBlockResponse
import com.nmichail.wordly.android.features.news.data.dto.NewsResponse
import com.nmichail.wordly.android.features.news.domain.entity.News
import com.nmichail.wordly.android.features.news.domain.entity.NewsContentBlock

fun NewsResponse.toEntity(): News =
	News(
		id = id,
		title = title,
		subtitle = subtitle,
		publishedAt = publishedAt,
		readingMinutes = readingMinutes,
		author = author,
		imageUrl = imageUrl,
		content = content.mapNotNull { it.toEntity() },
	)

private fun NewsContentBlockResponse.toEntity(): NewsContentBlock? =
	when (type) {
		"paragraph" -> NewsContentBlock.Paragraph(text = text)
		"quote" -> NewsContentBlock.Quote(text = text)
		else -> null
	}