package com.nmichail.wordly.android.core.preferences.data.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.jsonCacheDataStore: DataStore<Preferences> by preferencesDataStore(
	name = JSON_CACHE_PREFERENCES,
)

private const val JSON_CACHE_PREFERENCES = "wordly_page_cache_preferences"

/**
 * Thin TTL JSON store over Preferences DataStore.
 * Feature DataSources own keys and fetch-or-cache policy; this only persists JSON.
 */
interface JsonCacheStore {

	suspend fun <T> getFresh(
		key: String,
		type: Class<T>,
		ttlMs: Long = DEFAULT_TTL_MS,
	): T?

	suspend fun <T> getAny(
		key: String,
		type: Class<T>,
	): T?

	suspend fun <T> put(
		key: String,
		value: T,
	)

	suspend fun clear(key: String)

	companion object {
		const val DEFAULT_TTL_MS: Long = 30L * 60L * 1000L
	}
}

@Singleton
class JsonCacheStoreImpl @Inject constructor(
	context: Context,
	private val gson: Gson,
) : JsonCacheStore {

	private val dataStore = context.jsonCacheDataStore

	override suspend fun <T> getFresh(
		key: String,
		type: Class<T>,
		ttlMs: Long,
	): T? {
		val entry = readEntry(key) ?: return null
		if (System.currentTimeMillis() - entry.savedAtMs > ttlMs) {
			return null
		}
		return decode(entry.json, type)
	}

	override suspend fun <T> getAny(
		key: String,
		type: Class<T>,
	): T? {
		val entry = readEntry(key) ?: return null
		return decode(entry.json, type)
	}

	override suspend fun <T> put(
		key: String,
		value: T,
	) {
		writeEntry(
			key = key,
			json = gson.toJson(value),
			savedAtMs = System.currentTimeMillis(),
		)
	}

	override suspend fun clear(key: String) {
		val jsonKey = stringPreferencesKey(jsonKeyName(key))
		val tsKey = longPreferencesKey(timestampKeyName(key))
		dataStore.edit { prefs ->
			prefs.remove(jsonKey)
			prefs.remove(tsKey)
		}
	}

	private suspend fun readEntry(key: String): CacheEntry? {
		val prefs = dataStore.data.first()
		val json = prefs[stringPreferencesKey(jsonKeyName(key))] ?: return null
		val savedAtMs = prefs[longPreferencesKey(timestampKeyName(key))] ?: return null
		return CacheEntry(json = json, savedAtMs = savedAtMs)
	}

	private suspend fun writeEntry(
		key: String,
		json: String,
		savedAtMs: Long,
	) {
		val jsonKey = stringPreferencesKey(jsonKeyName(key))
		val tsKey = longPreferencesKey(timestampKeyName(key))
		dataStore.edit { prefs ->
			prefs[jsonKey] = json
			prefs[tsKey] = savedAtMs
		}
	}

	private fun <T> decode(
		json: String,
		type: Class<T>,
	): T? =
		try {
			gson.fromJson(json, type)
		} catch (_: Exception) {
			null
		}

	private data class CacheEntry(
		val json: String,
		val savedAtMs: Long,
	)

	private companion object {
		fun jsonKeyName(key: String): String = "${key}_json"

		fun timestampKeyName(key: String): String = "${key}_ts"
	}
}

suspend fun <T> JsonCacheStore.getOrFetch(
	key: String,
	type: Class<T>,
	ttlMs: Long = JsonCacheStore.DEFAULT_TTL_MS,
	fetch: suspend () -> T,
): T {
	getFresh(key = key, type = type, ttlMs = ttlMs)?.let { return it }
	return try {
		val remote = fetch()
		put(key = key, value = remote)
		remote
	} catch (error: Exception) {
		getAny(key = key, type = type) ?: throw error
	}
}
