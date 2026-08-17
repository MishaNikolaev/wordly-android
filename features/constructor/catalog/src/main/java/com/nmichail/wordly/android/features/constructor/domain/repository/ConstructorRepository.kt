package com.nmichail.wordly.android.features.constructor.domain.repository

import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog

interface ConstructorRepository {

    suspend fun getCatalog(): ConstructorCatalog
}