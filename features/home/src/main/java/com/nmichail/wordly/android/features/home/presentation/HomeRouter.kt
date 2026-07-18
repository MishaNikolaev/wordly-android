package com.nmichail.wordly.android.features.home.presentation

import com.nmichail.wordly.android.features.home.domain.entity.News

interface HomeRouter {

	fun navigateToReview()

	fun navigateToNews(news: News)
}
