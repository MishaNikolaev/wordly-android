package com.nmichail.wordly.android.features.news.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultNewsDetailComponentFactory @Inject constructor(
	private val newsDetailStoreFactory: NewsDetailStoreFactory,
) : NewsDetailComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		newsId: String,
		newsDetailRouter: NewsDetailRouter,
	): NewsDetailComponent =
		DefaultNewsDetailComponent(
			componentContext = componentContext,
			newsId = newsId,
			newsDetailStoreFactory = newsDetailStoreFactory,
			newsDetailRouter = newsDetailRouter,
		)
}