package com.nmichail.wordly.android.features.materials.data.datasource

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

interface MaterialsLocalDataSource {

	fun isViewed(id: String): Boolean

	fun markViewed(id: String)

	fun getReaction(id: String): String?

	fun setReaction(id: String, reaction: String?)
}

@Singleton
class MaterialsLocalDataSourceImpl @Inject constructor(
	context: Context,
) : MaterialsLocalDataSource {

	companion object {

		const val MATERIALS_PREFERENCES = "MATERIALS_PREFERENCES"
		private const val VIEWED_IDS_KEY = "VIEWED_IDS_KEY"
	}

	private val sharedPreferences: SharedPreferences =
		context.getSharedPreferences(MATERIALS_PREFERENCES, Context.MODE_PRIVATE)

	override fun isViewed(id: String): Boolean =
		viewedIds().contains(id)

	override fun markViewed(id: String) {
		val updated = viewedIds().toMutableSet().apply { add(id) }
		sharedPreferences.edit()
			.putStringSet(VIEWED_IDS_KEY, updated)
			.apply()
	}

	override fun getReaction(id: String): String? =
		sharedPreferences.getString(reactionKey(id), null)?.takeIf { it.isNotBlank() }

	override fun setReaction(id: String, reaction: String?) {
		sharedPreferences.edit().apply {
			if (reaction.isNullOrBlank()) {
				remove(reactionKey(id))
			} else {
				putString(reactionKey(id), reaction)
			}
			apply()
		}
	}

	private fun viewedIds(): Set<String> =
		sharedPreferences.getStringSet(VIEWED_IDS_KEY, emptySet())?.toSet().orEmpty()

	private fun reactionKey(id: String): String = "REACTION_$id"
}
