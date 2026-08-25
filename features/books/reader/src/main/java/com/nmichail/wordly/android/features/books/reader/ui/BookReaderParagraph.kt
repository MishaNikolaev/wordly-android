package com.nmichail.wordly.android.features.books.reader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nmichail.wordly.android.features.books.reader.ui.component.LookupReadingText
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookParagraph
import com.nmichail.wordly.android.features.books.reader.domain.entity.BookTranslation

@Composable
internal fun BookReaderParagraph(
    paragraph: BookParagraph,
    translatedText: String?,
    showTranslation: Boolean,
    onSelectWord: (String) -> Unit,
    onContentTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showTranslation && !translatedText.isNullOrBlank()) {
        Text(
            text = translatedText,
            style = WuiTypography.bookReaderBody,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
				.fillMaxWidth()
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onContentTap,
				),
        )
    } else {
        val segments = remember(paragraph.segments) {
            paragraph.segments.toLookupSegments()
        }
        LookupReadingText(
            segments = segments,
            onSelectWord = onSelectWord,
            onContentTap = onContentTap,
            modifier = modifier,
        )
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