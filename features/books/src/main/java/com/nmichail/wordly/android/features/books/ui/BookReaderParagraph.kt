package com.nmichail.wordly.android.features.books.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nmichail.wordly.android.features.books.ui.component.LookupReadingText
import com.nmichail.wordly.android.features.books.ui.component.LookupTextSegment
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.books.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegment
import com.nmichail.wordly.android.features.books.domain.entity.BookTextSegmentType
import com.nmichail.wordly.android.features.books.domain.entity.BookTranslation

@Composable
internal fun BookReaderParagraph(
	paragraph: BookParagraph,
	translatedText: String?,
	showTranslation: Boolean,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	if (showTranslation && !translatedText.isNullOrBlank()) {
		Text(
			text = translatedText,
			style = WuiTypography.bookReaderBody,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = modifier.fillMaxWidth(),
		)
	} else {
		val segments = remember(paragraph.segments) {
			paragraph.segments.toLookupSegments()
		}
		LookupReadingText(
			segments = segments,
			onSelectWord = onSelectWord,
			modifier = modifier,
		)
	}
}

private fun List<BookTextSegment>.toLookupSegments(): List<LookupTextSegment> =
	mapNotNull { segment ->
		when (segment.type) {
			BookTextSegmentType.TEXT -> LookupTextSegment(
				text = segment.text,
				lookupId = null,
			)
			BookTextSegmentType.LOOKUP_WORD -> {
				val wordId = segment.id ?: return@mapNotNull null
				LookupTextSegment(
					text = segment.text,
					lookupId = wordId,
				)
			}
		}
	}

internal fun translationFor(
	translation: BookTranslation?,
	paragraphId: String,
): String? =
	translation
		?.paragraphs
		?.firstOrNull { it.id == paragraphId }
		?.text