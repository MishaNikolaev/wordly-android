package com.nmichail.wordly.android.shared.catalog

const val CATALOG_LEVEL_SECTION_PREFIX = "Под ваш уровень · "
const val CATALOG_OTHER_LEVELS_TITLE = "Другие уровни"

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

/** Rebuilds sections so items with matching CEFR badge go under "for your level". */
fun <Section, Item> regroupCatalogSectionsByLevel(
	sections: List<Section>,
	level: String,
	getItems: (Section) -> List<Item>,
	getBadge: (Item) -> String?,
	createSection: (title: String, items: List<Item>) -> Section,
): List<Section> {
	val allItems = sections.flatMap(getItems)
	if (allItems.isEmpty()) return emptyList()

	val normalizedLevel = level.trim()
	val forLevel = allItems.filter { item ->
		getBadge(item)?.equals(normalizedLevel, ignoreCase = true) == true
	}
	val others = allItems.filter { item ->
		getBadge(item)?.equals(normalizedLevel, ignoreCase = true) != true
	}

	return buildList {
		if (forLevel.isNotEmpty()) {
			add(createSection("$CATALOG_LEVEL_SECTION_PREFIX$normalizedLevel", forLevel))
		}
		if (others.isNotEmpty()) {
			add(createSection(CATALOG_OTHER_LEVELS_TITLE, others))
		}
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
