package com.nmichail.wordly.android.features.home.data.datasource

import com.nmichail.wordly.android.core.preferences.data.cache.JsonCacheStore
import com.nmichail.wordly.android.core.preferences.data.cache.getOrFetch
import com.nmichail.wordly.android.features.home.data.api.HomeApi
import com.nmichail.wordly.android.features.home.data.dto.HomeResponse
import javax.inject.Inject

interface HomeDataSource {

	suspend fun getHome(): HomeResponse

	suspend fun invalidateCache()
}

class HomeDataSourceImpl @Inject constructor(
	private val api: HomeApi,
	private val cache: JsonCacheStore,
) : HomeDataSource {

	override suspend fun getHome(): HomeResponse =
		cache.getOrFetch(
			key = KEY,
			type = HomeResponse::class.java,
		) {
			api.getHome()
		}

	override suspend fun invalidateCache() {
		cache.clear(key = KEY)
	}

	private companion object {
		const val KEY = "page_home"
	}
}
