package com.nmichail.wordly.android.features.materials.article.data.api

import com.nmichail.wordly.android.features.materials.article.data.dto.MaterialDetailDto
import com.nmichail.wordly.android.features.materials.article.data.dto.MaterialReactionRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MaterialArticleApi {

	@GET("/api/materials/{id}")
	suspend fun getMaterial(
		@Path("id") id: String,
	): MaterialDetailDto

	@POST("/api/materials/{id}/reaction")
	suspend fun setReaction(
		@Path("id") id: String,
		@Body request: MaterialReactionRequestDto,
	): MaterialDetailDto
}
