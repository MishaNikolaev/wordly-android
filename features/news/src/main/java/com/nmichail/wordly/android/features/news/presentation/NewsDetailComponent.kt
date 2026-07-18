package com.nmichail.wordly.android.features.news.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.news.domain.entity.NewsContentBlock

interface NewsDetailComponent {

	val model: Value<State>

	fun handleBack()

	fun handleShare()

	fun handleBookmark()

	data class State(
		val title: String = "",
		val publishedAt: String = "",
		val readingMinutes: Int = 0,
		val author: String = "",
		val imageUrl: String? = null,
		val content: List<NewsContentBlock> = emptyList(),
		val isLoading: Boolean = true,
	)

	sealed interface Label {

		data object Close : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			newsId: String,
			newsDetailRouter: NewsDetailRouter,
		): NewsDetailComponent
	}
}
