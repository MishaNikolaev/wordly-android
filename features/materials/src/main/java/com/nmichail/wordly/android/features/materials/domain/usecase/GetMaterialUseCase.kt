package com.nmichail.wordly.android.features.materials.domain.usecase

import com.nmichail.wordly.android.features.materials.domain.repository.MaterialsRepository
import javax.inject.Inject

class GetMaterialUseCase @Inject constructor(
	private val materialsRepository: MaterialsRepository,
) {

	suspend operator fun invoke(id: String) = materialsRepository.getMaterial(id = id)
}