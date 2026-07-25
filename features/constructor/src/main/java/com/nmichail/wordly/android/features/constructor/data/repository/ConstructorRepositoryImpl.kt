package com.nmichail.wordly.android.features.constructor.data.repository

import com.nmichail.wordly.android.features.constructor.data.api.ConstructorApi
import com.nmichail.wordly.android.features.constructor.data.mapper.toEntity
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.domain.repository.ConstructorRepository
import javax.inject.Inject

class ConstructorRepositoryImpl @Inject constructor(
	private val constructorApi: ConstructorApi,
) : ConstructorRepository {

	override suspend fun getCatalog(): ConstructorCatalog =
		constructorApi.getCatalog().toEntity()

	override suspend fun getSession(themeId: String): ConstructorSession =
		constructorApi.getSession(themeId).toEntity()
}
