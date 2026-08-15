package com.nmichail.wordly.android.shared.catalog

const val CATALOG_LEVEL_SECTION_PREFIX = "Под ваш уровень · "

fun matchesCatalogSearch(
	title: String,
	subtitle: String,
	badge: String?,
	query: String,
): Boolean =
	title.contains(query, ignoreCase = true) ||
		subtitle.contains(query, ignoreCase = true) ||
		badge.orEmpty().contains(query, ignoreCase = true)

fun <Section, Item> filterCatalogSections(
	sections: List<Section>,
	query: String,
	getItems: (Section) -> List<Item>,
	itemMatches: (Item, String) -> Boolean,
	copyWithItems: (Section, List<Item>) -> Section,
): List<Section> {
	val normalized = query.trim()
	if (normalized.isEmpty()) return sections

	return sections.mapNotNull { section ->
		val items = getItems(section).filter { item -> itemMatches(item, normalized) }
		if (items.isEmpty()) {
			null
		} else {
			copyWithItems(section, items)
		}
	}
}

fun <Section> updateCatalogLevelSectionTitles(
	sections: List<Section>,
	level: String,
	getTitle: (Section) -> String,
	copyWithTitle: (Section, String) -> Section,
): List<Section> =
	sections.map { section ->
		val title = getTitle(section)
		if (title.startsWith(CATALOG_LEVEL_SECTION_PREFIX)) {
			copyWithTitle(section, "$CATALOG_LEVEL_SECTION_PREFIX$level")
		} else {
			section
		}
	}

fun <Section, Item> findCatalogItem(
	sections: List<Section>,
	getItems: (Section) -> List<Item>,
	predicate: (Item) -> Boolean,
): Item? {
	for (section in sections) {
		for (item in getItems(section)) {
			if (predicate(item)) {
				return item
			}
		}
	}
	return null
}
