package com.nmichail.wordly.android.features.home.domain.repository

import com.nmichail.wordly.android.features.home.domain.entity.HomePayload

interface HomeRepository {

	suspend fun getHome(): HomePayload
}
