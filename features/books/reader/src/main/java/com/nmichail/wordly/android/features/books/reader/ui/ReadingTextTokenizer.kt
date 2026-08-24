package com.nmichail.wordly.android.features.books.reader.ui

import com.nmichail.wordly.android.features.books.reader.domain.FREE_WORD_PREFIX
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.reader.domain.normalizeLookupWord
import com.nmichail.wordly.android.features.books.reader.ui.component.LookupTextSegment

private val TOKEN_REGEX = Regex("""[A-Za-z]+(?:['’][A-Za-z]+)?|[^A-Za-z]+""")

internal fun List<BookTextSegment>.toLookupSegments(): List<LookupTextSegment> =
	flatMap { segment ->
		when (segment.type) {
			BookTextSegmentType.TEXT -> segment.text.toFreeWordSegments()
			BookTextSegmentType.LOOKUP_WORD -> {
				val wordId = segment.id ?: return@flatMap emptyList()
				listOf(
					LookupTextSegment(
						text = segment.text,
						lookupId = wordId,
						underlined = true,
					),
				)
			}
		}
	}

internal fun String.toFreeWordSegments(): List<LookupTextSegment> =
	TOKEN_REGEX.findAll(this).map { match ->
		val token = match.value
		if (token.firstOrNull()?.isLetter() == true) {
			LookupTextSegment(
				text = token,
				lookupId = FREE_WORD_PREFIX + normalizeLookupWord(token),
				underlined = false,
			)
		} else {
			LookupTextSegment(
				text = token,
				lookupId = null,
				underlined = false,
			)
		}
	}.toList()
