package com.nmichail.wordly.android.component.wui.pagination

import androidx.compose.ui.text.TextStyle

fun wuiPaginateMeasuredTexts(
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

	return wuiPaginateItemHeights(
		itemHeightsPx = heights,
		pageHeightPx = pageHeightPx,
		spacingPx = spacingPx,
		firstPageReservedPx = firstPageReservedPx,
	)
}
