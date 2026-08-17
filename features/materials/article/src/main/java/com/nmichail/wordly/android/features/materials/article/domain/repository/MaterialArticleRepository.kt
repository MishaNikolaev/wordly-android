package com.nmichail.wordly.android.features.materials.article.domain.repository

import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail

interface MaterialArticleRepository {

    suspend fun getMaterial(id: String): MaterialDetail
}