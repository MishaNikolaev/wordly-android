package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import kotlin.math.max
import kotlin.math.min

data class LookupTextSegment(
	val text: String,
	val lookupId: String?,
)

@Composable
fun LookupReadingText(
	segments: List<LookupTextSegment>,
	onSelectWord: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val bodyStyle = WordlyTypography.bookReaderBody
	val textColor = MaterialTheme.colorScheme.onBackground
	val underlineColor = WordlyColors.BookLookupUnderline
	val currentOnSelectWord by rememberUpdatedState(onSelectWord)
	val annotated = remember(segments) { buildLookupAnnotatedString(segments) }
	var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
	@Suppress("MagicNumber")
	val dashEffect = remember {
		PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
	}

	Text(
		text = annotated,
		style = bodyStyle,
		color = textColor,
		modifier = modifier
			.fillMaxWidth()
			.drawBehind {
				val layout = textLayoutResult ?: return@drawBehind
				annotated.getStringAnnotations(
					tag = "lookup",
					start = 0,
					end = annotated.length,
				).forEach { annotation ->
					drawLookupUnderline(
						layout = layout,
						start = annotation.start,
						end = annotation.end,
						color = underlineColor,
						dashEffect = dashEffect,
					)
				}
			}
			.pointerInput(annotated) {
				detectTapGestures { offset ->
					val layout = textLayoutResult ?: return@detectTapGestures
					val position = layout.getOffsetForPosition(offset)
					if (position !in 0 until annotated.length) return@detectTapGestures
					annotated.getStringAnnotations(
						tag = "lookup",
						start = position,
						end = position,
					).firstOrNull()?.item?.let(currentOnSelectWord)
				}
			},
		onTextLayout = { textLayoutResult = it },
	)
}

private fun buildLookupAnnotatedString(
	segments: List<LookupTextSegment>,
): AnnotatedString =
	buildAnnotatedString {
		segments.forEach { segment ->
			val lookupId = segment.lookupId
			if (lookupId == null) {
				append(segment.text)
			} else {
				val start = length
				append(segment.text)
				addStringAnnotation(
					tag = "lookup",
					annotation = lookupId,
					start = start,
					end = length,
				)
			}
		}
	}

private fun DrawScope.drawLookupUnderline(
	layout: TextLayoutResult,
	start: Int,
	end: Int,
	color: Color,
	dashEffect: PathEffect,
) {
	if (start >= end) return
	val firstLine = layout.getLineForOffset(start)
	val lastLine = layout.getLineForOffset(end - 1)
	for (line in firstLine..lastLine) {
		val lineStart = max(start, layout.getLineStart(line))
		val lineEndExclusive = min(end, layout.getLineEnd(line, visibleEnd = true))
		if (lineStart >= lineEndExclusive) continue
		val left = layout.getHorizontalPosition(lineStart, usePrimaryDirection = true)
		val right = layout.getHorizontalPosition(
			offset = (lineEndExclusive - 1).coerceAtLeast(lineStart),
			usePrimaryDirection = true,
		).let { position ->
			val box = layout.getBoundingBox((lineEndExclusive - 1).coerceAtLeast(lineStart))
			max(position, box.right)
		}
		val y = layout.getLineBottom(line) - 2f
		drawLine(
			color = color,
			start = Offset(left, y),
			end = Offset(right, y),
			strokeWidth = 2f,
			pathEffect = dashEffect,
		)
	}
}
