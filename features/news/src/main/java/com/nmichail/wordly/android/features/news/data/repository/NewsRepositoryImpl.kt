package com.nmichail.wordly.android.features.news.data.repository

import com.nmichail.wordly.android.features.news.data.api.NewsApi
import com.nmichail.wordly.android.features.news.data.mapper.toEntity
import com.nmichail.wordly.android.features.news.domain.entity.News
import com.nmichail.wordly.android.features.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
	private val newsApi: NewsApi,
) : NewsRepository {

	override suspend fun getNews(id: String): News =
		newsApi.getNews(id).toEntity()
}