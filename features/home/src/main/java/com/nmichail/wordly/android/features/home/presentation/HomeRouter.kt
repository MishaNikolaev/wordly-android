package com.nmichail.wordly.android.features.home.presentation

import com.nmichail.wordly.android.features.news.domain.entity.News

interface HomeRouter {

	fun navigateToReview()

	fun navigateToCards()

	fun navigateToConstructor()

	fun navigateToBooks()

	fun navigateToNews(news: News)
}