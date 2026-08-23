package com.nmichail.wordly.android.features.constructor.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.constructor.data.api.ConstructorApi
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorCatalogResponse
import javax.inject.Inject

interface ConstructorDataSource {

	suspend fun getCatalog(): ConstructorCatalogResponse
}

class ConstructorDataSourceImpl @Inject constructor(
	private val api: ConstructorApi,
	private val cache: JsonCacheStore,
) : ConstructorDataSource {

	override suspend fun getCatalog(): ConstructorCatalogResponse =
		cache.getOrFetch(
			key = KEY,
			type = ConstructorCatalogResponse::class.java,
		) {
			api.getCatalog()
		}

	private companion object {
		const val KEY = "page_constructor"
	}
}
