package com.nmichail.wordly.android.features.books.data.mapper

import com.nmichail.wordly.android.features.books.data.dto.BookContentResponse
import com.nmichail.wordly.android.features.books.data.dto.BookParagraphResponse
import com.nmichail.wordly.android.features.books.data.dto.BookTextSegmentResponse
import com.nmichail.wordly.android.features.books.data.dto.BookTranslatedParagraphResponse
import com.nmichail.wordly.android.features.books.data.dto.BookTranslationResponse
import com.nmichail.wordly.android.features.books.data.dto.BookWordDefinitionResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksCatalogResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksItemResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksLevelBannerResponse
import com.nmichail.wordly.android.features.books.data.dto.BooksSectionResponse
import com.nmichail.wordly.android.features.books.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslatedParagraph
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.domain.entity.BookWordDefinition
import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.features.books.domain.entity.BooksLevelBanner
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection

private const val SEGMENT_TYPE_TEXT = "text"
private const val SEGMENT_TYPE_LOOKUP_WORD = "lookupWord"

fun BooksCatalogResponse.toEntity(): BooksCatalog =
	BooksCatalog(
		title = title,
		searchPlaceholder = searchPlaceholder,
		levelBanner = levelBanner?.toEntity(),
		sections = sections.map { it.toEntity() },
	)

fun BookContentResponse.toEntity(): BookContent =
	BookContent(
		id = id,
		title = title,
		author = author,
		coverUrl = coverUrl,
		paragraphs = paragraphs.map { it.toEntity() },
	)

fun BookTranslationResponse.toEntity(): BookTranslation =
	BookTranslation(
		paragraphs = paragraphs.map { it.toEntity() },
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
		items = items.map { it.toEntity() },
	)

private fun BooksItemResponse.toEntity(): BooksItem =
	BooksItem(
		id = id,
		title = title,
		subtitle = subtitle,
		badge = badge,
		imageUrl = imageUrl,
	)

private fun BookParagraphResponse.toEntity(): BookParagraph =
	BookParagraph(
		id = id,
		segments = segments.mapNotNull { it.toEntity() },
	)

private fun BookTextSegmentResponse.toEntity(): BookTextSegment? =
	when (type) {
		SEGMENT_TYPE_TEXT -> BookTextSegment(
			type = BookTextSegmentType.TEXT,
			text = text,
			id = null,
			definition = null,
		)
		SEGMENT_TYPE_LOOKUP_WORD -> {
			val segmentId = id ?: return null
			val wordDefinition = definition?.toEntity() ?: return null
			BookTextSegment(
				type = BookTextSegmentType.LOOKUP_WORD,
				text = text,
				id = segmentId,
				definition = wordDefinition,
			)
		}
		else -> null
	}

private fun BookWordDefinitionResponse.toEntity(): BookWordDefinition =
	BookWordDefinition(
		word = word,
		phonetic = phonetic,
		translation = translation,
		partOfSpeech = partOfSpeech,
		example = example,
	)

private fun BookTranslatedParagraphResponse.toEntity(): BookTranslatedParagraph =
	BookTranslatedParagraph(
		id = id,
		text = text,
	)