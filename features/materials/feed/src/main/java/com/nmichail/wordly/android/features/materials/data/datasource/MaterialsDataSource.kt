package com.nmichail.wordly.android.features.materials.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.materials.data.api.MaterialsApi
import com.nmichail.wordly.android.features.materials.data.dto.MaterialsCatalogDto
import javax.inject.Inject

interface MaterialsDataSource {

	suspend fun getMaterials(category: String?): MaterialsCatalogDto
}

class MaterialsDataSourceImpl @Inject constructor(
	private val api: MaterialsApi,
	private val cache: JsonCacheStore,
) : MaterialsDataSource {

	override suspend fun getMaterials(category: String?): MaterialsCatalogDto =
		cache.getOrFetch(
			key = materialsKey(category ?: "all"),
			type = MaterialsCatalogDto::class.java,
		) {
			api.getMaterials(category = category)
		}

	private companion object {
		fun materialsKey(filter: String): String = "page_materials_$filter"
	}
}
