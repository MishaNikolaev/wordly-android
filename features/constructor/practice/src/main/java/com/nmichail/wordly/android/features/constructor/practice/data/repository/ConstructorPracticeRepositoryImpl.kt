package com.nmichail.wordly.android.features.constructor.practice.data.repository

import com.nmichail.wordly.android.features.constructor.practice.data.api.ConstructorPracticeApi
import com.nmichail.wordly.android.features.constructor.practice.data.mapper.toEntity
import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.practice.domain.repository.ConstructorPracticeRepository
import javax.inject.Inject

class ConstructorPracticeRepositoryImpl @Inject constructor(
    private val constructorPracticeApi: ConstructorPracticeApi,
) : ConstructorPracticeRepository {

    override suspend fun getSession(themeId: String): ConstructorSession =
        constructorPracticeApi.getSession(themeId).toEntity()
}