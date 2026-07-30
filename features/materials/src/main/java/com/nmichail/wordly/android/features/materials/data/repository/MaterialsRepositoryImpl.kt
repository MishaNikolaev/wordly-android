package com.nmichail.wordly.android.features.materials.data.repository

import com.nmichail.wordly.android.features.materials.data.api.MaterialsApi
import com.nmichail.wordly.android.features.materials.data.mapper.toDomain
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsFilters
import com.nmichail.wordly.android.features.materials.domain.repository.MaterialsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialsRepositoryImpl @Inject constructor(
	private val materialsApi: MaterialsApi,
) : MaterialsRepository {

	override suspend fun getMaterials(filters: MaterialsFilters) =
		materialsApi.getMaterials(category = filters.filter.toApiCategory()).toDomain()

	override suspend fun getMaterial(id: String) = materialsApi.getMaterial(id = id).toDomain()
}

private fun MaterialFilter.toApiCategory(): String? =
	when (this) {
		MaterialFilter.All -> null
		MaterialFilter.Grammar -> "GRAMMAR"
		MaterialFilter.Idioms -> "IDIOMS"
		MaterialFilter.Conversational -> "CONVERSATIONAL"
		MaterialFilter.Listening -> "LISTENING"
	}