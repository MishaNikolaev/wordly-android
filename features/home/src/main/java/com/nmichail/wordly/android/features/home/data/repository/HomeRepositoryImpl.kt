package com.nmichail.wordly.android.features.home.data.repository

import com.nmichail.wordly.android.features.home.data.api.HomeApi
import com.nmichail.wordly.android.features.home.data.mapper.toDomain
import com.nmichail.wordly.android.features.home.domain.entity.HomePayload
import com.nmichail.wordly.android.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
	private val homeApi: HomeApi,
) : HomeRepository {

	override suspend fun getHome(): HomePayload =
		homeApi.getHome().toDomain()
}
