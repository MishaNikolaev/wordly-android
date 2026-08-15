package com.nmichail.wordly.android.component.wui.pagination

fun wuiPaginateItemHeights(
	itemHeightsPx: List<Int>,
	pageHeightPx: Int,
	spacingPx: Int,
	firstPageReservedPx: Int = 0,
): List<IntRange> {
	if (itemHeightsPx.isEmpty()) return emptyList()
	if (pageHeightPx <= 0) return listOf(itemHeightsPx.indices)

	val spacing = spacingPx.coerceAtLeast(0)

	return buildList {
		var pageStart = 0
		var usedHeight = firstPageReservedPx.coerceAtLeast(0)
		var itemsOnPage = 0

		fun closePage(endExclusive: Int) {
			if (pageStart < endExclusive) {
				add(pageStart until endExclusive)
			}
			pageStart = endExclusive
			usedHeight = 0
			itemsOnPage = 0
		}

		itemHeightsPx.forEachIndexed { index, rawHeight ->
			val itemHeight = rawHeight.coerceAtLeast(0)
			val gap = when {
				itemsOnPage > 0 -> spacing
				usedHeight > 0 -> spacing
				else -> 0
			}
			val needed = gap + itemHeight
			val exceeds = usedHeight + needed > pageHeightPx

			when {
				!exceeds -> {
					usedHeight += needed
					itemsOnPage += 1
				}
				itemsOnPage > 0 -> {
					closePage(endExclusive = index)
					usedHeight = itemHeight
					itemsOnPage = 1
				}
				else -> {
					usedHeight += needed
					itemsOnPage = 1
				}
			}
		}

		closePage(endExclusive = itemHeightsPx.size)
	}
}
