package com.nmichail.wordly.android.features.materials.domain.repository

import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsCatalog
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsFilters

interface MaterialsRepository {

	suspend fun getMaterials(filters: MaterialsFilters): MaterialsCatalog
}
