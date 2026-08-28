package com.nmichail.wordly.android.features.materials.article.domain.repository

import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialReaction

interface MaterialArticleRepository {

	suspend fun getMaterial(id: String): MaterialDetail

	suspend fun getReaction(id: String): MaterialReaction?

	suspend fun setReaction(id: String, reaction: MaterialReaction?): MaterialDetail
}
