package com.nmichail.wordly.android.features.news.domain.repository

import com.nmichail.wordly.android.features.news.domain.entity.News

interface NewsRepository {

	suspend fun getNews(id: String): News
}
