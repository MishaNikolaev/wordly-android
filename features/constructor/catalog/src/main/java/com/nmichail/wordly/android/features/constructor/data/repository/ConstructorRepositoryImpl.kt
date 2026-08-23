package com.nmichail.wordly.android.features.constructor.data.repository

import com.nmichail.wordly.android.features.constructor.data.datasource.ConstructorDataSource
import com.nmichail.wordly.android.features.constructor.data.mapper.toEntity
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.repository.ConstructorRepository
import javax.inject.Inject

class ConstructorRepositoryImpl @Inject constructor(
	private val dataSource: ConstructorDataSource,
) : ConstructorRepository {

	override suspend fun getCatalog(): ConstructorCatalog =
		dataSource.getCatalog().toEntity()
}
