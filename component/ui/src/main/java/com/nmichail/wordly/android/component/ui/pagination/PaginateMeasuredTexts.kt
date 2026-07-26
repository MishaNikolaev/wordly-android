package com.nmichail.wordly.android.component.ui.pagination

import androidx.compose.ui.text.TextStyle

/**
 * Measures [primaryTexts] (and optional [alternateTexts] at the same indices) and packs
 * them into pages. When an alternate text is present, height is max(primary, alternate)
 * so page breaks stay stable when switching languages.
 */
fun paginateMeasuredTexts(
	primaryTexts: List<String>,
	alternateTexts: List<String?>,
	contentWidthPx: Int,
	pageHeightPx: Int,
	spacingPx: Int,
	firstPageReservedPx: Int,
	style: TextStyle,
	measure: (text: String, style: TextStyle, maxWidthPx: Int) -> Int,
): List<IntRange> {
	if (primaryTexts.isEmpty()) return emptyList()
	if (contentWidthPx <= 0 || pageHeightPx <= 0) {
		return listOf(primaryTexts.indices)
	}

	val heights = primaryTexts.mapIndexed { index, primary ->
		val primaryHeight = measure(primary, style, contentWidthPx)
		val alternate = alternateTexts.getOrNull(index)
		if (alternate.isNullOrBlank()) {
			primaryHeight
		} else {
			maxOf(primaryHeight, measure(alternate, style, contentWidthPx))
		}
	}

	return paginateItemHeights(
		itemHeightsPx = heights,
		pageHeightPx = pageHeightPx,
		spacingPx = spacingPx,
		firstPageReservedPx = firstPageReservedPx,
	).ifEmpty { listOf(primaryTexts.indices) }
}
