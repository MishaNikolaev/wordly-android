package com.nmichail.wordly.android.features.materials.domain.usecase

import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsCatalog
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsFilters
import com.nmichail.wordly.android.features.materials.domain.repository.MaterialsRepository
import javax.inject.Inject

class GetMaterialsUseCase @Inject constructor(
	private val materialsRepository: MaterialsRepository,
) {

	suspend operator fun invoke(filters: MaterialsFilters): MaterialsCatalog =
		materialsRepository.getMaterials(filters = filters)
}