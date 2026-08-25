package com.nmichail.wordly.android.features.books.data.mapper

import com.nmichail.wordly.android.features.books.data.dto.BooksCatalogResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksItemResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksLevelBannerResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksSectionResponse
import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.features.books.domain.entity.BooksLevelBanner
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection

fun BooksCatalogResponse.toEntity(): BooksCatalog =
	BooksCatalog(
		title = title,
		searchPlaceholder = searchPlaceholder,
		levelBanner = levelBanner?.toEntity(),
		sections = sections.map(BooksSectionResponse::toEntity),
	)

private fun BooksLevelBannerResponse.toEntity(): BooksLevelBanner =
	BooksLevelBanner(
		text = text,
		levelLabel = levelLabel,
		levels = levels,
	)

private fun BooksSectionResponse.toEntity(): BooksSection =
	BooksSection(
		title = title,
		items = items.map(BooksItemResponse::toEntity),
	)

private fun BooksItemResponse.toEntity(): BooksItem =
	BooksItem(
		id = id,
		title = title,
		author = author?.takeIf { it.isNotBlank() } ?: subtitle,
		genre = genre,
		category = category,
		badge = badge,
		imageUrl = imageUrl,
		description = description.orEmpty(),
		readersCount = readersCount,
	)