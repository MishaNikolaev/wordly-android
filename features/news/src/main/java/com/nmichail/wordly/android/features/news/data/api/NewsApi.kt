package com.nmichail.wordly.android.features.news.data.api

import com.nmichail.wordly.android.features.news.data.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApi {

	@GET("/api/news/{id}")
	suspend fun getNews(@Path("id") id: String): NewsResponse
}
