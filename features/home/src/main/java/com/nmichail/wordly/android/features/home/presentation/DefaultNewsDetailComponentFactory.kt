package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.home.domain.entity.News
import javax.inject.Inject

internal class DefaultNewsDetailComponentFactory @Inject constructor() :
	NewsDetailComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		news: News,
		onBack: () -> Unit,
	): NewsDetailComponent =
		DefaultNewsDetailComponent(
			componentContext = componentContext,
			news = news,
			onBack = onBack,
		)
}
