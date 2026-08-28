package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import com.nmichail.wordly.android.core.fakenetwork.R
import com.nmichail.wordly.android.core.fakenetwork.getJson
import org.json.JSONObject

internal fun Context.findMaterialCatalogItem(materialId: String): JSONObject? {
	val catalog = JSONObject(getJson(R.raw.get_materials))
	val items = catalog.getJSONArray("items")
	for (index in 0 until items.length()) {
		val item = items.getJSONObject(index)
		if (item.optString("id") == materialId) {
			return item
		}
	}
	return null
}

internal fun Context.materialDetailJson(
	materialId: String,
	likesOverride: Int? = null,
	dislikesOverride: Int? = null,
	userReaction: String? = null,
): String? {
	val item = findMaterialCatalogItem(materialId) ?: return null
	val detail = JSONObject()
		.put("id", materialId)
		.put("category", item.optString("category"))
		.put("typeLabel", item.optString("typeLabel", "Статья"))
		.put("title", item.optString("title"))
		.put("description", item.optString("description"))
		.put("readingMinutes", item.optInt("readingMinutes", 5))
		.put("dateLabel", item.optString("dateLabel"))
		.put("level", item.optString("level"))
		.put("likes", likesOverride ?: item.optInt("likes"))
		.put("dislikes", dislikesOverride ?: item.optInt("dislikes"))
		.put("status", item.optString("status", "NEW"))
	if (item.has("photoUrl") && !item.isNull("photoUrl")) {
		detail.put("photoUrl", item.get("photoUrl"))
	}
	if (userReaction != null) {
		detail.put("userReaction", userReaction)
	}
	return detail.toString()
}
