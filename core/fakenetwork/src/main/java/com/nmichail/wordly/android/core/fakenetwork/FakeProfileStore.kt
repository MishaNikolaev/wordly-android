package com.nmichail.wordly.android.core.fakenetwork

import android.content.Context
import org.json.JSONObject

object FakeProfileStore {

	@Volatile
	private var profileJson: String? = null

	fun getProfile(context: Context): String {
		profileJson?.let { return it }
		val loaded = context.getJson(R.raw.profile_ok)
		profileJson = loaded
		return loaded
	}

	fun updateProfile(context: Context, requestBody: String?): String {
		val current = JSONObject(getProfile(context))
		if (!requestBody.isNullOrBlank()) {
			val patch = JSONObject(requestBody)
			patch.keys().forEach { key ->
				if (!patch.isNull(key)) {
					current.put(key, patch.get(key))
				}
			}
		}
		val updated = current.toString()
		profileJson = updated
		return updated
	}

	fun reset() {
		profileJson = null
	}
}
