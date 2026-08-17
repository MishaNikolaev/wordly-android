package com.nmichail.wordly.android.features.materials.article.data.api

import com.nmichail.wordly.android.features.materials.article.data.dto.MaterialDetailDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MaterialArticleApi {

	@GET("api/materials/{id}")
	suspend fun getMaterial(
		@Path("id") id: String,
	): MaterialDetailDto
}