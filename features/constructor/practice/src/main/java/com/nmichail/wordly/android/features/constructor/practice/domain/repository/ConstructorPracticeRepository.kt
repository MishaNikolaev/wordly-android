package com.nmichail.wordly.android.features.constructor.practice.domain.repository

import com.nmichail.wordly.android.features.constructor.practice.domain.entity.ConstructorSession

interface ConstructorPracticeRepository {

    suspend fun getSession(themeId: String): ConstructorSession
}