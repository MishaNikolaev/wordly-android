package com.nmichail.wordly.android.core.fakenetwork

import org.json.JSONObject

internal object FakeEnglishLevelStore {

	@Volatile
	var level: String = DEFAULT_LEVEL
		private set

	fun update(level: String) {
		this.level = level
	}

	fun applyToCatalogJson(json: String): String {
		val catalog = JSONObject(json)
		catalog.optJSONObject("levelBanner")?.put("levelLabel", level)
		val sections = catalog.optJSONArray("sections") ?: return catalog.toString()
		for (index in 0 until sections.length()) {
			val section = sections.optJSONObject(index) ?: continue
			val title = section.optString("title")
			if (title.startsWith(LEVEL_SECTION_PREFIX)) {
				section.put("title", "$LEVEL_SECTION_PREFIX$level")
			}
		}
		return catalog.toString()
	}

	fun applyToProfileJson(json: String): String {
		val profile = JSONObject(json)
		profile.put("englishLevel", level)
		return profile.toString()
	}

	fun applyToSessionJson(json: String): String {
		val session = JSONObject(json)
		session.put("englishLevel", level)
		return session.toString()
	}

	private const val DEFAULT_LEVEL = "B2"
	private const val LEVEL_SECTION_PREFIX = "Под ваш уровень · "
}
