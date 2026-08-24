package com.nmichail.wordly.android.features.home.domain.repository

import com.nmichail.wordly.android.features.home.domain.entity.Home

interface HomeRepository {

	suspend fun getHome(): Home

	suspend fun invalidateCache()
}