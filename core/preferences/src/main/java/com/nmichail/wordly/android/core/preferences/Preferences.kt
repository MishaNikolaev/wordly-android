package com.nmichail.wordly.android.core.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> convertObjectFromJson(gson: Gson, objectInJson: String): T? =
	try {
		gson.fromJson(objectInJson, T::class.java)
	} catch (_: Exception) {
		null
	}

inline fun <reified T> SharedPreferences.Editor.convertToJsonAndPut(
	gson: Gson,
	key: String,
	data: T,
) {
	putString(key, gson.toJson(data)).apply()
}

inline fun <reified T> convertListObjectFromJson(gson: Gson, objectInJson: String): List<T>? =
	try {
		val type = object : TypeToken<List<T>>() {}.type
		gson.fromJson(objectInJson, type)
	} catch (_: Exception) {
		null
	}

inline fun <reified T> SharedPreferences.Editor.convertToJsonAndPut(
	gson: Gson,
	key: String,
	data: List<T>,
) {
	val type = object : TypeToken<List<T>>() {}.type
	putString(key, gson.toJson(data, type)).apply()
}

inline fun <reified K, reified V> convertMapObjectFromJson(
	gson: Gson,
	objectInJson: String,
): Map<K, V>? =
	try {
		val type = object : TypeToken<Map<K, V>>() {}.type
		gson.fromJson(objectInJson, type)
	} catch (_: Exception) {
		null
	}

inline fun <reified K, reified V> SharedPreferences.Editor.convertToJsonAndPut(
	gson: Gson,
	key: String,
	data: Map<K, V>,
) {
	val type = object : TypeToken<Map<K, V>>() {}.type
	putString(key, gson.toJson(data, type)).apply()
}
