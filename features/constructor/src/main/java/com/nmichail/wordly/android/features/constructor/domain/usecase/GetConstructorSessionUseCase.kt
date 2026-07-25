package com.nmichail.wordly.android.features.constructor.domain.usecase

import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.domain.repository.ConstructorRepository
import javax.inject.Inject

class GetConstructorSessionUseCase @Inject constructor(
	constructorRepository: ConstructorRepository,
) : suspend (String) -> ConstructorSession by constructorRepository::getSession
