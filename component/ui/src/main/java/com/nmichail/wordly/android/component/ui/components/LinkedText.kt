package com.nmichail.wordly.android.component.ui.components

import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun LinkedText(
	text: String,
	onLinkClick: (String) -> Unit,
	modifier: Modifier = Modifier,
	linkText: String? = null,
	style: TextStyle = MaterialTheme.typography.labelSmall,
	textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
	linkColor: Color = MaterialTheme.colorScheme.secondary,
	textAlign: TextAlign = TextAlign.Center,
	maxLines: Int = Int.MAX_VALUE,
	overflow: TextOverflow = TextOverflow.Clip,
	onLinkLongClick: (String) -> Unit = {},
) {
	var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
	val currentOnLinkClick by rememberUpdatedState(onLinkClick)
	val currentOnLinkLongClick by rememberUpdatedState(onLinkLongClick)
	val linkStyles = TextLinkStyles(
		style = SpanStyle(
			color = linkColor,
			textDecoration = TextDecoration.Underline,
		),
	)

	val annotatedString = rememberAnnotatedString(
		text = text,
		linkText = linkText,
		linkStyles = linkStyles,
		onLinkClick = currentOnLinkClick,
	)

	val pressIndicator = Modifier.pointerInput(annotatedString) {
		detectTapGestures(
			onLongPress = { offset ->
				val position = textLayoutResult
					?.getOffsetForPosition(offset)
					?: return@detectTapGestures

				if (position !in 0 until annotatedString.length) return@detectTapGestures

				annotatedString.linkTagAt(position)?.let(currentOnLinkLongClick)
			},
		)
	}

	Text(
		text = annotatedString,
		style = style,
		color = textColor,
		textAlign = textAlign,
		modifier = modifier.then(pressIndicator),
		maxLines = maxLines,
		overflow = overflow,
		onTextLayout = { textLayoutResult = it },
	)
}

@Composable
private fun rememberAnnotatedString(
	text: String,
	linkText: String?,
	linkStyles: TextLinkStyles,
	onLinkClick: (String) -> Unit,
): AnnotatedString = remember(text, linkText, linkStyles) {
	if (linkText != null) {
		buildLinkedString(text, linkText, linkStyles, onLinkClick)
	} else {
		buildWebLinkedString(text, linkStyles, onLinkClick)
	}
}

private fun buildWebLinkedString(
	text: String,
	linkStyles: TextLinkStyles,
	onLinkClick: (String) -> Unit,
): AnnotatedString =
	AnnotatedString.Builder(text).apply {
		val spannable = SpannableString(text).apply { Linkify.addLinks(this, Linkify.WEB_URLS) }

		spannable.getSpans(0, spannable.length, URLSpan::class.java).forEach { span ->
			val startIndex = spannable.getSpanStart(span)
			val endIndex = spannable.getSpanEnd(span)
			addLink(
				clickable = LinkAnnotation.Clickable(
					tag = span.url,
					styles = linkStyles,
					linkInteractionListener = { onLinkClick(span.url) },
				),
				start = startIndex,
				end = endIndex,
			)
		}
	}.toAnnotatedString()

private fun buildLinkedString(
	text: String,
	linkText: String,
	linkStyles: TextLinkStyles,
	onLinkClick: (String) -> Unit,
): AnnotatedString {
	val startIndex = text.indexOf(linkText, ignoreCase = true)
	if (startIndex < 0) {
		return AnnotatedString(text)
	}

	return AnnotatedString.Builder(text).apply {
		addLink(
			clickable = LinkAnnotation.Clickable(
				tag = linkText,
				styles = linkStyles,
				linkInteractionListener = { onLinkClick(linkText) },
			),
			start = startIndex,
			end = startIndex + linkText.length,
		)
	}.toAnnotatedString()
}

private fun AnnotatedString.linkTagAt(position: Int): String? =
	getLinkAnnotations(start = position, end = position)
		.firstOrNull()
		?.let { it.item as? LinkAnnotation.Clickable }
		?.tag
