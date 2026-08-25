package com.nmichail.wordly.android.component.wui.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.R
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import kotlin.math.roundToInt

@Composable
fun WuiBookReadingProgress(
	currentPage: Int,
	pageCount: Int,
	onPageChange: (Int) -> Unit,
	modifier: Modifier = Modifier,
) {
	var showPageNumbers by rememberSaveable { mutableStateOf(false) }
	val percent = bookReadingProgressPercent(currentPage = currentPage, pageCount = pageCount)
	val fraction = bookReadingProgressFraction(currentPage = currentPage, pageCount = pageCount)
	val colorScheme = MaterialTheme.colorScheme
	val label = if (showPageNumbers) {
		stringResource(
			R.string.book_reading_progress_page,
			currentPage + 1,
			pageCount.coerceAtLeast(1),
		)
	} else {
		stringResource(R.string.book_reading_progress_percent, percent)
	}

	Column(
		modifier = modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		BookReadingProgressTrack(
			fraction = fraction,
			pageCount = pageCount,
			onPageChange = onPageChange,
			fillColor = colorScheme.primary,
			trackColor = colorScheme.surfaceVariant,
		)
		Text(
			text = label,
			style = WuiTypography.bookReaderProgress,
			color = colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth()
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					role = Role.Button,
					onClick = { showPageNumbers = !showPageNumbers },
				),
		)
	}
}

@Composable
private fun BookReadingProgressTrack(
	fraction: Float,
	pageCount: Int,
	onPageChange: (Int) -> Unit,
	fillColor: Color,
	trackColor: Color,
	modifier: Modifier = Modifier,
) {
	val thumbSize = 12.dp
	val trackHeight = 4.dp
	val density = LocalDensity.current

	BoxWithConstraints(
		modifier = modifier
			.fillMaxWidth()
			.height(thumbSize)
			.pointerInput(pageCount) {
				fun pageAt(x: Float): Int =
					bookReadingProgressPageIndex(
						fraction = (x / size.width).coerceIn(0f, 1f),
						pageCount = pageCount,
					)

				detectTapGestures { offset ->
					onPageChange(pageAt(offset.x))
				}
				detectDragGestures { change, _ ->
					change.consume()
					onPageChange(pageAt(change.position.x))
				}
			},
		contentAlignment = Alignment.CenterStart,
	) {
		val trackWidthPx = with(density) { maxWidth.toPx() }
		val thumbSizePx = with(density) { thumbSize.toPx() }
		val thumbOffsetPx = ((trackWidthPx - thumbSizePx) * fraction)
			.coerceIn(0f, (trackWidthPx - thumbSizePx).coerceAtLeast(0f))

		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(trackHeight)
				.align(Alignment.Center)
				.clip(RoundedCornerShape(percent = 50))
				.background(trackColor),
		) {
			Box(
				modifier = Modifier
					.fillMaxHeight()
					.fillMaxWidth(fraction = fraction)
					.clip(RoundedCornerShape(percent = 50))
					.background(fillColor),
			)
		}
		Box(
			modifier = Modifier
				.offset(x = with(density) { thumbOffsetPx.toDp() })
				.size(thumbSize)
				.clip(CircleShape)
				.background(fillColor),
		)
	}
}

fun bookReadingProgressPercent(currentPage: Int, pageCount: Int): Int {
	val fraction = bookReadingProgressFraction(currentPage = currentPage, pageCount = pageCount)
	return (fraction * PERCENT_SCALE).roundToInt().coerceIn(0, PERCENT_MAX)
}

fun bookReadingProgressFraction(currentPage: Int, pageCount: Int): Float {
	val safePageCount = pageCount.coerceAtLeast(1)
	val safeCurrent = currentPage.coerceIn(0, safePageCount - 1)
	return (safeCurrent + 1).toFloat() / safePageCount.toFloat()
}

fun bookReadingProgressPageIndex(fraction: Float, pageCount: Int): Int {
	val safePageCount = pageCount.coerceAtLeast(1)
	return ((fraction * safePageCount).toInt())
		.coerceIn(0, safePageCount - 1)
}

private const val PERCENT_SCALE = 100f
private const val PERCENT_MAX = 100

@WuiPreviews
@Composable
private fun WuiBookReadingProgressPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		WuiBookReadingProgress(
			currentPage = 0,
			pageCount = 20,
			onPageChange = {},
			modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
		)
	}
}