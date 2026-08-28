package com.nmichail.wordly.android.features.materials.article.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.materials.article.data.api.MaterialArticleApi
import com.nmichail.wordly.android.features.materials.article.data.dto.MaterialDetailDto
import com.nmichail.wordly.android.features.materials.article.data.dto.MaterialReactionRequestDto
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialReaction
import com.nmichail.wordly.android.features.materials.data.datasource.MaterialsLocalDataSource
import javax.inject.Inject

interface MaterialArticleDataSource {

	suspend fun getMaterial(id: String): MaterialDetailDto

	suspend fun getReaction(id: String): MaterialReaction?

	suspend fun setReaction(id: String, reaction: MaterialReaction?): MaterialDetailDto
}

class MaterialArticleDataSourceImpl @Inject constructor(
	private val api: MaterialArticleApi,
	private val cache: JsonCacheStore,
	private val localDataSource: MaterialsLocalDataSource,
) : MaterialArticleDataSource {

	override suspend fun getMaterial(id: String): MaterialDetailDto {
		localDataSource.markViewed(id)
		val base = loadBaseDetail(id)
		return base.withUserReaction(localDataSource.getReaction(id))
	}

	override suspend fun getReaction(id: String): MaterialReaction? =
		localDataSource.getReaction(id).toReaction()

	override suspend fun setReaction(
		id: String,
		reaction: MaterialReaction?,
	): MaterialDetailDto {
		localDataSource.setReaction(id = id, reaction = reaction?.toApiValue())
		try {
			api.setReaction(
				id = id,
				request = MaterialReactionRequestDto(reaction = reaction?.toApiValue()),
			)
		} catch (_: Exception) {
		}
		val base = loadBaseDetail(id)
		return base.withUserReaction(reaction?.toApiValue())
	}

	private suspend fun loadBaseDetail(id: String): MaterialDetailDto {
		val cachedOrRemote = cache.getOrFetch(
			key = detailKey(id),
			type = MaterialDetailDto::class.java,
		) {
			api.getMaterial(id = id)
		}
		val base = cachedOrRemote.asBaseCounts()
		cache.put(key = detailKey(id), value = base)
		return base
	}

	private companion object {
		const val CACHE_VERSION = 3

		fun detailKey(id: String): String = "page_material_detail_v${CACHE_VERSION}_$id"

		fun MaterialReaction.toApiValue(): String =
			when (this) {
				MaterialReaction.Like -> "LIKE"
				MaterialReaction.Dislike -> "DISLIKE"
			}

		fun String?.toReaction(): MaterialReaction? =
			when (this?.uppercase()) {
				"LIKE" -> MaterialReaction.Like
				"DISLIKE" -> MaterialReaction.Dislike
				else -> null
			}

		fun MaterialDetailDto.asBaseCounts(): MaterialDetailDto {
			val current = userReaction?.uppercase()
			return copy(
				likes = (likes - if (current == "LIKE") 1 else 0).coerceAtLeast(0),
				dislikes = (dislikes - if (current == "DISLIKE") 1 else 0).coerceAtLeast(0),
				userReaction = null,
			)
		}

		fun MaterialDetailDto.withUserReaction(reaction: String?): MaterialDetailDto {
			val normalized = reaction?.uppercase()
			return copy(
				likes = likes + if (normalized == "LIKE") 1 else 0,
				dislikes = dislikes + if (normalized == "DISLIKE") 1 else 0,
				userReaction = normalized,
			)
		}
	}
}
