package com.nmichail.wordly.android.features.materials.article.data.repository

import com.nmichail.wordly.android.features.materials.article.data.api.MaterialArticleApi
import com.nmichail.wordly.android.features.materials.article.data.mapper.toDomain
import com.nmichail.wordly.android.features.materials.article.domain.repository.MaterialArticleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialArticleRepositoryImpl @Inject constructor(
	private val materialArticleApi: MaterialArticleApi,
) : MaterialArticleRepository {

	override suspend fun getMaterial(id: String) =
		materialArticleApi.getMaterial(id = id).toDomain()
}
