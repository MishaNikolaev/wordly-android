package com.nmichail.wordly.android.features.materials.data.api

import com.nmichail.wordly.android.features.materials.data.dto.MaterialsCatalogDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MaterialsApi {

	@GET("api/materials")
	suspend fun getMaterials(
		@Query("category") category: String? = null,
	): MaterialsCatalogDto
}
