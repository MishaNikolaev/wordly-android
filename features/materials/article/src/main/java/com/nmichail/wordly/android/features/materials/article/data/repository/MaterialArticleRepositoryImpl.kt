package com.nmichail.wordly.android.features.materials.article.data.repository

import com.nmichail.wordly.android.features.materials.article.data.datasource.MaterialArticleDataSource
import com.nmichail.wordly.android.features.materials.article.data.mapper.toDomain
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialReaction
import com.nmichail.wordly.android.features.materials.article.domain.repository.MaterialArticleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialArticleRepositoryImpl @Inject constructor(
	private val dataSource: MaterialArticleDataSource,
) : MaterialArticleRepository {

	override suspend fun getMaterial(id: String): MaterialDetail =
		dataSource.getMaterial(id = id).toDomain()

	override suspend fun getReaction(id: String): MaterialReaction? =
		dataSource.getReaction(id = id)

	override suspend fun setReaction(id: String, reaction: MaterialReaction?): MaterialDetail =
		dataSource.setReaction(id = id, reaction = reaction).toDomain()
}
