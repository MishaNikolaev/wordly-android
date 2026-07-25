package com.nmichail.wordly.android.features.constructor.domain.repository

import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession

interface ConstructorRepository {

	suspend fun getCatalog(): ConstructorCatalog

	suspend fun getSession(themeId: String): ConstructorSession
}