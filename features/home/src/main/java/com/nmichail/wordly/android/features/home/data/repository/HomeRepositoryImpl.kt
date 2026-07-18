package com.nmichail.wordly.android.features.home.data.repository

import com.nmichail.wordly.android.features.home.data.api.HomeApi
import com.nmichail.wordly.android.features.home.data.mapper.toEntity
import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
	private val homeApi: HomeApi,
) : HomeRepository {

	override suspend fun getHome(): Home =
		homeApi.getHome().toEntity()
}
