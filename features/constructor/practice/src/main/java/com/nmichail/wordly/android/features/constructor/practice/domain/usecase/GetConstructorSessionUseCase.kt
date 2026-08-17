package com.nmichail.wordly.android.features.constructor.practice.domain.usecase

import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.practice.domain.repository.ConstructorPracticeRepository
import javax.inject.Inject

class GetConstructorSessionUseCase @Inject constructor(
    constructorPracticeRepository: ConstructorPracticeRepository,
) : suspend (String) -> ConstructorSession by constructorPracticeRepository::getSession