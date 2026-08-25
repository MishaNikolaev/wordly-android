package com.nmichail.wordly.android.features.books.detail.data.mapper

import com.nmichail.wordly.android.features.books.detail.data.dto.BookDetailResponse
import com.nmichail.wordly.android.features.books.detail.domain.entity.BookDetail

fun BookDetailResponse.toDomain(): BookDetail =
	BookDetail(
		id = id,
		title = title,
		author = author,
		coverUrl = coverUrl,
		description = description.orEmpty(),
		genre = genre,
		category = category,
		level = badge,
	)