package com.nmichail.wordly.android.features.constructor.domain.usecase

import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.repository.ConstructorRepository
import javax.inject.Inject

class GetConstructorCatalogUseCase @Inject constructor(
    constructorRepository: ConstructorRepository,
) : suspend () -> ConstructorCatalog by constructorRepository::getCatalog