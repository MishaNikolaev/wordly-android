package com.nmichail.wordly.android.features.materials.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.materials.data.api.MaterialsApi
import com.nmichail.wordly.android.features.materials.data.dto.MaterialItemDto
import com.nmichail.wordly.android.features.materials.data.dto.MaterialsCatalogDto
import javax.inject.Inject

interface MaterialsDataSource {

	suspend fun getMaterials(category: String?): MaterialsCatalogDto
}

class MaterialsDataSourceImpl @Inject constructor(
	private val api: MaterialsApi,
	private val cache: JsonCacheStore,
	private val localDataSource: MaterialsLocalDataSource,
) : MaterialsDataSource {

	override suspend fun getMaterials(category: String?): MaterialsCatalogDto {
		val catalog = cache.getOrFetch(
			key = materialsKey(category ?: "all"),
			type = MaterialsCatalogDto::class.java,
		) {
			api.getMaterials(category = category)
		}
		return catalog.copy(
			items = catalog.items.map { item -> item.withLocalViewedStatus() },
		)
	}

	private fun MaterialItemDto.withLocalViewedStatus(): MaterialItemDto =
		if (localDataSource.isViewed(id)) copy(status = "READ") else this

	private companion object {
		fun materialsKey(filter: String): String = "page_materials_$filter"
	}
}
