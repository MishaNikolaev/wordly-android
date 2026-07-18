package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.home.domain.entity.News

internal class DefaultNewsDetailComponent(
	componentContext: ComponentContext,
	news: News,
	private val onBack: () -> Unit,
) : ComponentContext by componentContext,
	NewsDetailComponent {

	override val model: Value<NewsDetailComponent.State> = MutableValue(
		NewsDetailComponent.State(
			title = news.title,
			subtitle = news.subtitle,
			body = news.body,
			publishedAt = news.publishedAt,
		),
	)

	override fun handleBack() {
		onBack()
	}
}
