package com.nmichail.wordly.android.features.books.reader.data.mapper

import com.nmichail.wordly.android.features.books.reader.data.dto.BookContentResponse
import com.nmichail.wordly.android.features.books.reader.data.dto.BookParagraphResponse
import com.nmichail.wordly.android.features.books.reader.data.dto.BookTextSegmentResponse
import com.nmichail.wordly.android.features.books.reader.data.dto.BookTranslatedParagraphResponse
import com.nmichail.wordly.android.features.books.reader.data.dto.BookTranslationResponse
import com.nmichail.wordly.android.features.books.reader.data.dto.BookWordDefinitionResponse
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookContent
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslatedParagraph
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookWordDefinition

private const val SEGMENT_TYPE_TEXT = "text"
private const val SEGMENT_TYPE_LOOKUP_WORD = "lookupWord"

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

private fun BookTranslatedParagraphResponse.toEntity(): BookTranslatedParagraph =
    BookTranslatedParagraph(
        id = id,
        // Drop English leftovers that were mistakenly stored as "translation".
        text = text.takeIf(::hasCyrillic).orEmpty(),
    )

private fun hasCyrillic(value: String): Boolean =
    value.any { it in '\u0400'..'\u04FF' }

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