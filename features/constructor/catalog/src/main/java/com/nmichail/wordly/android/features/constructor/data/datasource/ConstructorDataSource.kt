package com.nmichail.wordly.android.features.constructor.data.datasource

import com.nmichail.wordly.android.features.constructor.data.api.ConstructorApi
import com.nmichail.wordly.android.features.constructor.data.dto.ConstructorCatalogResponse
import javax.inject.Inject

interface ConstructorDataSource {

	suspend fun getCatalog(): ConstructorCatalogResponse
}

class ConstructorDataSourceImpl @Inject constructor(
	private val api: ConstructorApi,
) : ConstructorDataSource {

	override suspend fun getCatalog(): ConstructorCatalogResponse = api.getCatalog()
}
