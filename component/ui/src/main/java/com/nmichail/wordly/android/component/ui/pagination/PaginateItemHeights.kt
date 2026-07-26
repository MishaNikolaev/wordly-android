package com.nmichail.wordly.android.component.ui.pagination

/**
 * Packs item heights into pages that fit [pageHeightPx].
 *
 * [firstPageReservedPx] is subtracted from the first page only (e.g. a hint row).
 * Oversized items still occupy a page alone.
 */
fun paginateItemHeights(
	itemHeightsPx: List<Int>,
	pageHeightPx: Int,
	spacingPx: Int,
	firstPageReservedPx: Int = 0,
): List<IntRange> {
	if (itemHeightsPx.isEmpty() || pageHeightPx <= 0) {
		return emptyList()
	}

	val pages = mutableListOf<IntRange>()
	var pageStart = 0
	var usedHeight = 0
	var isFirstPage = true

	itemHeightsPx.forEachIndexed { index, height ->
		val itemHeight = height.coerceAtLeast(0)
		val capacity = if (isFirstPage) {
			(pageHeightPx - firstPageReservedPx.coerceAtLeast(0)).coerceAtLeast(0)
		} else {
			pageHeightPx
		}
		val spacing = if (usedHeight == 0) 0 else spacingPx
		val needed = spacing + itemHeight
		val wouldExceed = usedHeight > 0 && usedHeight + needed > capacity

		if (wouldExceed) {
			pages += pageStart until index
			pageStart = index
			usedHeight = itemHeight
			isFirstPage = false
		} else {
			usedHeight += needed
		}
	}

	pages += pageStart until itemHeightsPx.size
	return pages
}
